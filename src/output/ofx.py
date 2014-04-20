import xml.etree.ElementTree as ET
from model.bankStatement import BankStatement
from model.transaction import Transaction
import sys

class Ofx:

    def appendElement( self, parent, tag, text = '' ):

        tagElement = ET.SubElement( parent, tag )

        if text != '':
            tagElement.text = text

        return tagElement

    def appendStatusElement( self, parent ):

        status = ET.SubElement( parent, 'STATUS' )

        self.appendElement( status, 'CODE', '0' )
        self.appendElement( status, 'SEVERITY', 'INFO' )

    def createTransaction( self, parent, transaction ):
        transactionElement = self.appendElement( parent, 'STMTTRN' )

        self.appendElement( transactionElement, 'TRNTYPE', transaction.type )
        self.appendElement( transactionElement, 'DTPOSTED', transaction.date )
        self.appendElement( transactionElement, 'TRNAMT', transaction.amount )
        self.appendElement( transactionElement, 'FITID', str(transaction.date) + transaction.amount.replace( '-', '' ) )
        self.appendElement( transactionElement, 'NAME', transaction.name )
        if transaction.account:
            self.appendElement( transactionElement, 'BANKACCTTO', transaction.account )
        self.appendElement( transactionElement, 'memo', transaction.memo )

    def createXmlFile( self, filename, bankStatement ):

        root = ET.Element('OFX')

        signOnMessage = ET.SubElement( root, 'SIGNONMSGSRSV1' )

        signOn = ET.SubElement( signOnMessage, 'SONRS' )

        self.appendStatusElement( signOn )

        self.appendElement( signOn, 'DTSERVER', bankStatement.dateTime )
        self.appendElement( signOn, 'LANGUAGE', bankStatement.language )
        self.appendElement( signOn, 'DTPROFUP', bankStatement.dateTime )
        self.appendElement( signOn, 'DTACCTUP', bankStatement.dateTime )

        institution = ET.SubElement( signOn, 'FI' )

        self.appendElement( institution, 'ORG', 'NCH' )
        self.appendElement( institution, 'FID', '1001' )

        bankMessage = ET.SubElement( root, 'BANKMSGSRSV1' )
        statementMessage = self.appendElement( bankMessage, 'STMTTRNRS' )

        self.appendElement( statementMessage, 'TRNUID', '1001' )
        self.appendStatusElement( statementMessage )

        statements = self.appendElement( statementMessage, 'STMTRS' )

        self.appendElement( statements, 'CURDEF', bankStatement.currency )

        bankAccount = self.appendElement( statements, 'BANKACCTFROM' )

        self.appendElement( bankAccount, 'BANKID', '121099999' )
        self.appendElement( bankAccount, 'ACCTID', bankStatement.account )
        self.appendElement( bankAccount, 'ACCTTYPE', 'CHECKING' )

        transactionList = self.appendElement( statements, 'BANKTRANLIST' )

        self.appendElement( transactionList, 'DTSTART', bankStatement.dateStart )
        self.appendElement( transactionList, 'DTEND', bankStatement.dateEnd )

        for transaction in bankStatement.transactions:
            self.createTransaction( transactionList, transaction )

        tree = ET.ElementTree( root )
        tree.write( sys.stdout ) //filename
        

if __name__ == '__main__':

    ofx = Ofx()

    ofx.createXmlFile( 0 , 0 )
