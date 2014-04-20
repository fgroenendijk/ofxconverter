import csv
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
                        transaction.memo += row[ int(i) ]
                elif field[0] == 'type':
                    transaction.type = row[ int(field[1]) ]
                elif field[0] == 'name':
                    transaction.name = row[ int(field[1]) ]
                elif field[0] == 'account':
                    transaction.account = row[ int(field[1]) ]

            if lineCorrect:
                bankStatement.addTransaction( transaction )

    def __init__(self, fields):
        self.fields = fields
