import xml.etree.ElementTree as ET

class Ofx:

    def appendElement( self, parent, tag, text ):

        tagElement = ET.SubElement( parent, tag )

        if text:
            tagElement.text = text

        return tagElement

    def appendStatusElement( self, parent ):

        status = ET.SubElement( parent, 'STATUS' )

        appendElement( status, 'CODE', '0' )
        appendElement( status, 'SEVERITY', 'INFO' )

    def createTransaction( self, parent, transaction ):
        transactionElement = appendElement( parent, 'STMTTRN' )

        appendElement( transactionElement, 'TRNTYPE', transaction.type )
        appendElement( transactionElement, 'DTPOSTED', transaction.date )
        appendElement( transactionElement, 'TRNAMT', transaction.amount )
        appendElement( transactionElement, 'FITID', transaction.date + transaction.amount.replace( '-', '' ) )
        appendElement( transactionElement, 'NAME', transaction.name )
        if transaction.account:
            appendElement( transactionElement, 'BANKACCTTO', transaction.account )
        appendElement( transactionElement, 'memo', transaction.memo )

    def createXmlFile( self, filename, bankStatement ):

        root = ET.Element('OFX')

        signOnMessage = ET.SubElement( root, 'SIGNONMSGSRSV1' )

        signOn = ET.SubElement( signOnMessage, 'SONRS' )

        appendStatusElement( signOn )

        appendElement( signOn, 'DTSERVER', bankStatement.date )
        appendElement( signOn, 'LANGUAGE', bankStatement.language )
        appendElement( signOn, 'DTPROFUP', bankStatement.date )
        appendElement( signOn, 'DTACCTUP', bankStatement.date )

        institution = ET.SubElement( signOn, 'FI' )

        appendElement( institution, 'ORG', 'NCH' )
        appendElement( institution, 'FID', '1001' )

        bankMessage = ET.SubElement( root, 'BANKMSGSRSV1' )
        statementMessage = appendElement( bankMessage, 'STMTTRNRS' )

        appendElement( statementMessage, 'TRNUID', '1001' )
        appendStatusElement( statementMessage )

        statements = appendElement( statementMessage, 'STMTRS' )

        appendElement( statements, 'CURDEF', bankStatement.currency )

        bankAccount = appendElement( statements, 'BANKACCTFROM' )

        appendElement( bankAccount, 'BANKID', '121099999' )
        appendElement( bankAccount, 'ACCTID', bankStatement.account )
        appendElement( bankAccount, 'ACCTTYPE', 'CHECKING' )

        transactionList = appendElement( statements, 'BANKTRANLIST' )

        appendElement( transactionList, 'DTSTART', bankStatement.dateStart )
        appendElement( transactionList, 'DTEND', bankStatement.dateEnd )

        for bankStatement.transactions as transaction:
            createTransaction( transaction )            

        tree = ET.ElementTree( root )
        tree.write( sys.stdout ) //filename
        

if __name__ == '__main__':

    ofx = Ofx()

    ofx.createXmlFile( 0 , 0 )
