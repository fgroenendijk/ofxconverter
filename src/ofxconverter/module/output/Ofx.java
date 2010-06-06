/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.module.output;

import com.sun.xml.internal.stream.writers.XMLWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
/**
 *
 * @author Floris
 */
public class Ofx {

    private Document doc = null;

    private boolean createDocument(){
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.newDocument();
            return true;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Ofx.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private Element createRootElement( String name ){
        Element root = doc.createElement(name);
        doc.appendChild(root);
        return root;
    }

    private Element appendStatusElement( Element parent ){
            Element status = attachElement( parent, "STATUS");

            attachElement( status, "CODE", "0" );
            attachElement( status, "SEVERITY", "INFO" );

            return status;
    }

    /**
     * Will attach existing node to the parent
     * @param parent the parent element to attach the child element to
     * @param child the child node, this may never be null!
     * @return will return the newly added element
     */
    private void attachElement( Element parent, Element child ){
        if( child == null ) throw new IllegalArgumentException();
        parent.appendChild( child );
    }

    /**
     * Will create a new node from name and attach that to the parent
     * @param parent the parent element to attach the child element to
     * @param name the name of the child node, this may never be null!
     * @return will return the newly added element
     */
    private Element attachElement( Element parent, String name){
        return attachElement( parent, name, null );
    }

    /**
     * Will create a new node from name and attach that to the parent and create a text node
     * If text = null, no text will be added to the node
     * @param parent the parent element to attach the child element to
     * @param name the name of the child node, this may never be null!
     * @param text the optional text to add to the child node
     * @return will return the newly added element
     */
    private Element attachElement( Element parent, String name, String text){
        if( name == null ) throw new IllegalArgumentException("Name of child node may not be null!");
        Element child = doc.createElement(name);
        if( text != null ){
            child.setTextContent(text);
        }
        parent.appendChild(child);
        return child;
    }

    public boolean createXmlFile( File file ){
        boolean result = false;

        String dateTime = "20100602210441";
        String dateStart = "20100420";
        String dateEnd = "20100601";
        String language = "ENG";
        String currency = "EUR";
        String account = "657400114";

        /////////////////////////////
        //Creating an empty XML Document

        //We need a Document
        if( createDocument() ){

            ////////////////////////
            //Creating the XML tree

            //create the root element and add it to the document
            Element root = createRootElement( "OFX" );

            Element signOnMessage = attachElement( root, "SIGNONMSGSRSV1" );

            Element signOn = attachElement( signOnMessage, "SONRS");

            appendStatusElement( signOn );

            attachElement( signOn, "DTSERVER", dateTime );
            attachElement( signOn, "LANGUAGE", language );
            attachElement( signOn, "DTPROFUP", dateTime );
            attachElement( signOn, "DTACCTUP", dateTime );

            Element institution = attachElement( signOn, "FI" );

            attachElement( institution, "ORG", "NCH" );
            attachElement( institution, "FID", "1001" );

            Element bankMessage = attachElement( root, "BANKMSGSRSV1" );
            Element statementMessage = attachElement( bankMessage, "STMTTRNRS" );

            attachElement( statementMessage, "TRNUID", "1001" );
            appendStatusElement( statementMessage );

            Element statements = attachElement( statementMessage, "STMTRS");

            attachElement( statements, "CURDEF", currency );

            Element bankAccount = attachElement( statements, "BANKACCTFROM" );

            attachElement( bankAccount, "BANKID", "121099999" );
            attachElement( bankAccount, "ACCTID", account );
            attachElement( bankAccount, "ACCTTYPE", "CHECKING" );

            Element transactionList = attachElement( statements, "BANKTRANLIST" );

            attachElement( transactionList, "DTSTART", dateStart );
            attachElement( transactionList, "DTEND", dateEnd );

            Element transaction = attachElement( transactionList, "STMTTRN" );

            attachElement( transaction, "TRNTYPE", "OTHER" );
            attachElement( transaction, "DTPOSTED", "20100420" );
            attachElement( transaction, "TRNAMT", "820.97" );
            attachElement( transaction, "FITID", "20100420820.97" ); //dtposted+amount
            attachElement( transaction, "NAME", "TEST BLUE BILLYWIG B.V. SUMATRALAAN 45 1217 GP HILVERSUM SALARIS APRIL 2010" );
            attachElement( transaction, "BANKACCTTO", "122577876" );
            attachElement( transaction, "MEMO", "TEST BLUE BILLYWIG B.V. SUMATRALAAN 45 1217 GP HILVERSUM SALARIS APRIL 2010" );

            File outputFile = new File( file.getAbsolutePath().replaceAll("\\.\\w+$", ".ofx") );

            // Output the XML
            writeXmlFile( outputFile );

            // Print the xml
            System.out.println("Here's the xml:\n\n" + printXml());

        }

        return result;
    }

    private String printXml(){
        StringWriter writer = new StringWriter();
        xmlOutput( writer );
        if( writer != null ){
            return writer.toString();
        }
        else{
            return null;
        }
    }

    private boolean writeXmlFile( File file ){
        try {
            return xmlOutput( new FileWriter(file) );
        } catch (IOException ex) {
            Logger.getLogger(Ofx.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean xmlOutput( Writer writer ){
        {
            try {
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                trans.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
                StreamResult streamResult = new StreamResult(writer);
                DOMSource source = new DOMSource(doc);
                trans.transform(source, streamResult);
                return true;
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(Ofx.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(Ofx.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    // If string writer is used, the writer contains the string
                    if( !(writer instanceof StringWriter) ){
                        writer.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Ofx.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

}