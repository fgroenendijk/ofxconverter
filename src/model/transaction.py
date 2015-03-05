import re

class Transaction(object):

    @property
    def interestDate(self):
        return self.__date

    @interestDate.setter
    def interestDate(self, date):
        if int(date) < 0:
            self.__interestDate = 0
        else:
            self.__interestDate = int(date)

    @property
    def date(self):
        return self.__date

    @date.setter
    def date(self, date):
        if int(date) < 0:
            self.__date = 0
        else:
            self.__date = int(date)

    @property
    def amount(self):
        if self.debit and not re.search(u"^-",self.__amount):
            self.__amount = u"-" + self.__amount
        return self.__amount
    
    @amount.setter
    def amount(self, amount):
        if re.search(u"-?\d*\.?\d+,\d+$", amount):
            amount = amount.replace(u",",u":")
            amount = amount.replace(u".",u",")
            amount = amount.replace(u":",u".")

        self.__amount = amount.replace(u" ",u"")

        if re.search(u"^-",amount):
            self.debit = True

    @property
    def memo(self):
        return self.__memo
    
    @memo.setter
    def memo(self, memo):
            self.__memo = re.sub(u"\s{2,}",u" ",memo.strip())

    def rawType(self, rawType=u""):
        returnType = u"OTHER"

        # Machtiging
        if rawType == u"MA": 
            returnType = u"DIRECTDEBIT"

        # Telebankieren
        elif rawType == u"TB": 
            returnType = u"PAYMENT"

        # Betaalautomaat
        elif rawType == u"BA": 
            returnType = u"POS"

        # Geldautomaat (pin)
        elif rawType == u"GA": 
            returnType = u"ATM"

        else:
            if self.debit:
                returnType = u"DEBIT"
            else:
                returnType = u"CREDIT"

        return returnType

    @property
    def type(self):
        if self.__type == u"":
            self.rawType()
        return self.rawType( self.__type )

    @type.setter
    def type(self, type):
        self.__type = type

    def __init__(self):
        self.debit = False
        self.__interestDate = 0
        self.__date = 0
        self.__amount = u""
        self.__memo = u""
        self.__type = u"" 
        self.description = u""
        self.account = u""
        self.fields = [u"account",u"amount",u"currency",u"date",u"credit/debit",u"debit",u"interestDate",u"memo",u"description",u"type"]
