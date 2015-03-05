from datetime import datetime
from model.transaction import Transaction

class BankStatement(object):

    @property
    def failedStrings(self):
        return self.__failedStrings

    @failedStrings.setter
    def failedStrings(self, failedString):
        if len(self.__failedStrings) > 0:
            self.__failedStrings += u"\n"
        self.__failedStrings += failedString

    def addTransaction(self, transaction):
        date = transaction.date
        if self.dateStart > date:
            self.dateStart = date

        if self.dateEnd < date:
            self.dateEnd = date
        self.transactions.append( transaction )

    def __init__(self):
        self.__failedStrings = u""
        self.dateStart = 99999999
        self.dateEnd = 0
        self.currency = u""
        self.account = u""
        self.language = u"NL"
        self.transactions = []

        d = datetime.today()
        self.dateTime = d.strftime(u"%Y%m%d%H%M%S")
        
        
if __name__ == u"__main__":
    b = BankStatement()
    print b.dateTime
    b.failedStrings = u"test"
    b.failedStrings = u"test2"
    print b.failedStrings
    t = Transaction()
    t.amount = u"100.0"
    b.addTransaction( t )
    print b.transactions
    t = Transaction()
    t.amount = u"200.0"
    b.addTransaction( t )
    print b.transactions
    
    
