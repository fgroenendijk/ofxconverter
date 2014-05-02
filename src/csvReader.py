import csv
import re
from model.transaction import Transaction

class CsvReader:

    def readFile(self, fileName, bankStatement):
        file = csv.reader( open(fileName) )
        transaction = Transaction()
        for row in file:
            transaction = Transaction()
            lineCorrect = True
            for field in self.fields:
                if field[0] == 'interest':
                    if row[ int(field[1]) ].isdigit():
                        transaction.interestDate = row[ int(field[1]) ]
                    else:
                        lineCorrect = False
                elif field[0] == 'date':
                    if row[ int(field[1]) ].isdigit():
                        transaction.date = row[ int(field[1]) ]
                    else:
                        lineCorrect = False
                elif field[0] == 'amount':
                    transaction.amount = row[ int(field[1]) ]
                elif field[0] == 'memo':
                    for i in field[1].split():
                        transaction.memo += ' ' + row[ int(i) ]
                        print( transaction.memo )
                elif field[0] == 'type':
                    transaction.type = row[ int(field[1]) ]
                elif field[0] == 'name':
                    transaction.name = row[ int(field[1]) ]
                elif field[0] == 'account':
                    account = row[ int(field[1]) ]
                    if not re.search( r'\w{2}\d{2}\w{4}\d{7}\w{0,16}', account ):                        
                        account = account.replace(" ","")
                        print( account )
                        search = re.search(r'\w{2}\d{2}\w{4}.*'+account,' '.join(row))
                        print(search)
                        
                    transaction.account = account
                elif field[0] == 'credit/debit':
                    (fieldNumber,credit,debit) = field[1].split()
                    creditDebit = row[ int(fieldNumber) ]
                    if creditDebit == credit:
                        transaction.debit = False
                    elif creditDebit == debit:
                        transaction.debit = True

            if lineCorrect:
                bankStatement.addTransaction( transaction )

    def __init__(self, fields):
        self.fields = fields
