import xml.etree.ElementTree as ET
from model.bankStatement import BankStatement
from model.transaction import Transaction
import sys

class Ofx(object):

    def appendElement( self, parent, tag, text = u'' ):

        tagElement = ET.SubElement( parent, tag )

        if text != u'':
            tagElement.text = text

        return tagElement

    def appendStatusElement( self, parent ):

        status = ET.SubElement( parent, u'STATUS' )

        self.appendElement( status, u'CODE', u'0' )
        self.appendElement( status, u'SEVERITY', u'INFO' )

    def createTransaction( self, parent, transaction ):
        transactionElement = self.appendElement( parent, u'STMTTRN' )

        self.appendElement( transactionElement, u'TRNTYPE', transaction.type )
        self.appendElement( transactionElement, u'DTPOSTED', unicode(transaction.date) )
        self.appendElement( transactionElement, u'TRNAMT', transaction.amount )
        self.appendElement( transactionElement, u'FITID', unicode(transaction.date) + transaction.amount.replace( u'-', u'' ) )
        self.appendElement( transactionElement, u'NAME', transaction.description )
        if transaction.account:
            self.appendElement( transactionElement, u'BANKACCTTO', transaction.account )
        self.appendElement( transactionElement, u'MEMO', transaction.memo )

    def createXmlFile( self, filename, bankStatement ):

        root = ET.Element(u'OFX')

        signOnMessage = ET.SubElement( root, u'SIGNONMSGSRSV1' )

        signOn = ET.SubElement( signOnMessage, u'SONRS' )

        self.appendStatusElement( signOn )

        self.appendElement( signOn, u'DTSERVER', unicode(bankStatement.dateTime) )
        self.appendElement( signOn, u'LANGUAGE', bankStatement.language )
        self.appendElement( signOn, u'DTPROFUP', unicode(bankStatement.dateTime) )
        self.appendElement( signOn, u'DTACCTUP', unicode(bankStatement.dateTime) )

        institution = ET.SubElement( signOn, u'FI' )

        self.appendElement( institution, u'ORG', u'NCH' )
        self.appendElement( institution, u'FID', u'1001' )

        bankMessage = ET.SubElement( root, u'BANKMSGSRSV1' )
        statementMessage = self.appendElement( bankMessage, u'STMTTRNRS' )

        self.appendElement( statementMessage, u'TRNUID', u'1001' )
        self.appendStatusElement( statementMessage )

        statements = self.appendElement( statementMessage, u'STMTRS' )

        self.appendElement( statements, u'CURDEF', bankStatement.currency )

        bankAccount = self.appendElement( statements, u'BANKACCTFROM' )

        self.appendElement( bankAccount, u'BANKID', u'121099999' )
        self.appendElement( bankAccount, u'ACCTID', bankStatement.account )
        self.appendElement( bankAccount, u'ACCTTYPE', u'CHECKING' )

        transactionList = self.appendElement( statements, u'BANKTRANLIST' )

        self.appendElement( transactionList, u'DTSTART', unicode(bankStatement.dateStart) )
        self.appendElement( transactionList, u'DTEND', unicode(bankStatement.dateEnd) )

        for transaction in bankStatement.transactions:
            self.createTransaction( transactionList, transaction )

        tree = ET.ElementTree( root ) 
        tree.write( filename + u'.out' )
        

if __name__ == u'__main__':

    ofx = Ofx()

    ofx.createXmlFile( 0 , 0 )
