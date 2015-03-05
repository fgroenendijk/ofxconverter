import csv
import re
from model.transaction import Transaction
from io import open

class CsvReader(object):

    def readFile(self, fileName, bankStatement):
        file = csv.reader( open(fileName) )
        transaction = Transaction()
        for row in file:
            transaction = Transaction()
            lineCorrect = True
            for field in self.fields:
                if field[0] == u'interestDate':
                    if row[ int(field[1]) ].isdigit():
                        transaction.interestDate = row[ int(field[1]) ]
                    else:
                        lineCorrect = False
                elif field[0] == u'date':
                    if row[ int(field[1]) ].isdigit():
                        transaction.date = row[ int(field[1]) ]
                    else:
                        lineCorrect = False
                elif field[0] == u'amount':
                    transaction.amount = row[ int(field[1]) ]
                elif field[0] == u'memo':
                    for i in field[1].split():
                        transaction.memo += u' ' + row[ int(i) ]
                elif field[0] == u'type':
                    transaction.type = row[ int(field[1]) ]
                elif field[0] == u'description':
                    transaction.description = row[ int(field[1]) ].strip()
                elif field[0] == u'account':
                    account = row[ int(field[1]) ]
                    if not re.search( ur'\w{2}\d{2}\w{4}\d{7}\w{0,16}', account ):                        
                        account = account.replace(u" ",u"")
                        search = re.search(ur'\w{2}\d{2}\w{4}.*'+account,u' '.join(row))
                        
                    transaction.account = account
                elif field[0] == u'credit/debit':
                    (fieldNumber,credit,debit) = field[1].split()
                    creditDebit = row[ int(fieldNumber) ]
                    if creditDebit == credit:
                        transaction.debit = False
                    elif creditDebit == debit:
                        transaction.debit = True
                elif field[0] == u'currency' and len(bankStatement.currency) == 0:
                    bankStatement.currency = field[1]                   

            if lineCorrect:
                bankStatement.addTransaction( transaction )

    def __init__(self, fields):
        self.fields = fields
