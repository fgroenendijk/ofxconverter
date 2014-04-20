from config import Config
from input.bank import Bank
from output.ofx import Ofx
from model.bankStatement import BankStatement
from csvReader import CsvReader

if __name__ == '__main__':

    filename = 'input/test.csv'

    bank = Bank()

    ibans = bank.searchMainIban( filename )

    # If mainIban contains more than one iban,
    # let the user select which one is the main iban
    # Otherwise we know the bank this csv belongs to

    if len( ibans ) > 1:
        print( 'there\'s too many ibans' )
    elif len( ibans ) == 0:
        print( 'No ibans found' )
    else:
        ibanType = ibans[0][:8]

    config = Config()
    fields = config.getCurrentBank( ibanType )

    bankStatement = BankStatement()

    bankStatement.account = ibans[0]

    csvReader = CsvReader(fields)
    
    csvReader.readFile( filename, bankStatement )

    ofx = Ofx()

    ofx.createXmlFile( '', bankStatement )
    
    

    
