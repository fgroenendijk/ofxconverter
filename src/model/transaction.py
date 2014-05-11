import re

class Transaction:

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
        if self.debit and not re.search("^-",self.__amount):
            self.__amount = "-" + self.__amount
        return self.__amount
    
    @amount.setter
    def amount(self, amount):
        if re.search("-?\d*\.?\d+,\d+$", amount):
            amount = amount.replace(",",":")
            amount = amount.replace(".",",")
            amount = amount.replace(":",".")

        self.__amount = amount.replace(" ","")

        if re.search("^-",amount):
            self.debit = True

    @property
    def memo(self):
        return self.__memo
    
    @memo.setter
    def memo(self, memo):
            self.__memo = re.sub("\s{2,}"," ",memo.strip())

    def rawType(self, rawType=""):
        returnType = "OTHER"

        # Machtiging
        if rawType == "MA": 
            returnType = "DIRECTDEBIT"

        # Telebankieren
        elif rawType == "TB": 
            returnType = "PAYMENT"

        # Betaalautomaat
        elif rawType == "BA": 
            returnType = "POS"

        # Geldautomaat (pin)
        elif rawType == "GA": 
            returnType = "ATM"

        else:
            if self.debit:
                returnType = "DEBIT"
            else:
                returnType = "CREDIT"

        return returnType

    @property
    def type(self):
        if self.__type == "":
            self.rawType()
        return self.rawType( self.__type )

    @type.setter
    def type(self, type):
        self.__type = type

    def __init__(self):
        self.debit = False
        self.__interestDate = 0
        self.__date = 0
        self.__amount = ""
        self.__memo = ""
        self.__type = "" 
        self.description = ""
        self.account = ""
        self.fields = ["account","amount","date","credit/debit","interestDate","memo","description","type"]
