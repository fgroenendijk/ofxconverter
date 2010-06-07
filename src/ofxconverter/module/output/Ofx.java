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
import ofxconverter.structure.BankStatement;
import ofxconverter.structure.Transaction;
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

    /**
     * This function will create a new dom document
     * @return the result of the creation
     */
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

    public boolean createXmlFile( File file, BankStatement bankStatement ){
        boolean result = false;

        // Create a new dom document
        if( createDocument() ){

            ////////////////////////
            //Creating the XML tree

            //create the root element and add it to the document
            Element root = createRootElement( "OFX" );

            Element signOnMessage = attachElement( root, "SIGNONMSGSRSV1" );

            Element signOn = attachElement( signOnMessage, "SONRS");

            appendStatusElement( signOn );

            attachElement( signOn, "DTSERVER", bankStatement.getDateTime() );
            attachElement( signOn, "LANGUAGE", bankStatement.getLanguage().toString() );
            attachElement( signOn, "DTPROFUP", bankStatement.getDateTime() );
            attachElement( signOn, "DTACCTUP", bankStatement.getDateTime() );

            Element institution = attachElement( signOn, "FI" );

            attachElement( institution, "ORG", "NCH" );
            attachElement( institution, "FID", "1001" );

            Element bankMessage = attachElement( root, "BANKMSGSRSV1" );
            Element statementMessage = attachElement( bankMessage, "STMTTRNRS" );

            attachElement( statementMessage, "TRNUID", "1001" );
            appendStatusElement( statementMessage );

            Element statements = attachElement( statementMessage, "STMTRS");

            attachElement( statements, "CURDEF", bankStatement.getCurrency().toString() );

            Element bankAccount = attachElement( statements, "BANKACCTFROM" );

            attachElement( bankAccount, "BANKID", "121099999" );
            attachElement( bankAccount, "ACCTID", bankStatement.getAccount() );
            attachElement( bankAccount, "ACCTTYPE", "CHECKING" );

            Element transactionList = attachElement( statements, "BANKTRANLIST" );

            attachElement( transactionList, "DTSTART", Long.toString( bankStatement.getDateStart() ) );
            attachElement( transactionList, "DTEND", Long.toString( bankStatement.getDateEnd() ) );

            // For each transactionElement
            for( Transaction transaction: bankStatement.getTransactions() ){
                createTransaction( transactionList, transaction );
            }

            File outputFile = new File( file.getAbsolutePath().replaceAll("\\.\\w+$", ".ofx") );

            // Output the XML
            writeXmlFile( outputFile );
        }

        return result;
    }

    private boolean createTransaction( Element parent, Transaction transaction ){

        Element transactionElement = attachElement( parent, "STMTTRN" );

        attachElement( transactionElement, "TRNTYPE", transaction.getType() );
        attachElement( transactionElement, "DTPOSTED", Long.toString( transaction.getDate() ) );
        attachElement( transactionElement, "TRNAMT", transaction.getAmount() );
        attachElement( transactionElement, "FITID", transaction.getDate() + transaction.getAmount().replace("-", "") ); //dtposted+amount
        attachElement( transactionElement, "NAME", transaction.getName() );
        attachElement( transactionElement, "BANKACCTTO", transaction.getAccount() );
        attachElement( transactionElement, "MEMO", transaction.getMemo() );

        // TODO: check if the statement is correct
        return true;
    }

    public String printXml(){
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