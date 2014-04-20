import re

class Transaction:

    @property
    def interestDate(self):
        return self.__date

    @interestDate.setter
    def interestDate(self, date):
        if date < 0:
            self.__interestDate = 0
        else:
            self.__interestDate = date

    @property
    def date(self):
        return self.__date

    @date.setter
    def date(self, date):
        if date < 0:
            self.__date = 0
        else:
            self.__date = date

    @property
    def amount(self):
        return self.__amount
    
    @amount.setter
    def amount(self, amount):
        if re.search("-?\d*\.?\d+,\d+$", amount):
            amount = amount.replace(",",":")
            amount = amount.replace(".",",")
            amount = amount.replace(":",".")
            self.__amount = amount
        else:
            self.__amount = amount

    @property
    def memo(self):
        return self.__memo
    
    @memo.setter
    def memo(self, memo):
            self.__memo = re.sub("\s{2,}"," ",memo.rstrip())

    @property
    def rawType(self):
        return self.__type

    @rawType.setter
    def rawType(self, rawType, isDebet):
        returnType = "OTHER"

        # Machtiging
        if rawType.equalsIgnoreCase( "MA" ): 
            returnType = "DIRECTDEBIT"

        # Telebankieren
        elif rawType.equalsIgnoreCase( "TB"): 
            returnType = "PAYMENT"

        # Betaalautomaat
        elif rawType.equalsIgnoreCase( "BA"): 
            returnType = "POS"

        # Geldautomaat (pin)
        elif rawType.equalsIgnoreCase( "GA"): 
            returnType = "ATM"

        # Overschrijving
        elif rawType.equalsIgnoreCase( "OV"): 
            if isDebet:
                returnType = "DEBIT"
            else:
                returnType = "CREDIT"

        # Bijschrijving
        elif rawType.equalsIgnoreCase( "BY"): 
            if isDebet:
                returnType = "DEBIT"
            else:
                returnType = "CREDIT"

        # Diversen
        elif rawType.equalsIgnoreCase( "DA"):
            if isDebet:
                returnType = "DEBIT"
            else:
                returnType = "CREDIT"

        self.__type = returnType 

    def __init__(self):
        self.__interestDate = 0
        self.__date = 0
        self.__amount = ""
        self.__memo = ""
        self.__type = "" 
        self.name = ""
        self.account = ""
