from configobj import ConfigObj
from os.path import expanduser
import re
import csv

class Bank:

    def checkIban(self, iban, ibanSet):
        ibanCount = 1;
        for oldIbanTuple in ibanSet:
            print( 'tuple:', oldIbanTuple )
            (oldIban, count) = oldIbanTuple
            print( 'count', count, 'oldIban', oldIban )
            if oldIban == iban:
                ibanSet.remove( oldIbanTuple )
                ibanCount = count + 1
                break;

        ibanSet.add( (iban, ibanCount) )

        highestCount = 0
        ibanFound = []
        if( len( ibanSet ) > 1 ):
            for ibanTuple in ibanSet:
                (iban, count) = ibanTuple
                if count > highestCount:
                    highestCount = count

            for ibanTuple in ibanSet:
                (iban, count) = ibanTuple
                if count == highestCount:
                    ibanFound.append( iban )    

        return ibanFound

    def searchMainIban(self, fileName):
        ibans = set([])
        mainIban = []
        file = csv.reader( open(fileName) )
        for row in file:
            line = ','.join( row )
            match = re.search( r'\w{2}\d{2}\w{4}\d{7}\w{0,16}', line.replace( ' ', '' ) )
            if match:
                for column in row:
                    search = re.search(r'(\w{2}\d{2}\w{4}\d{7}\w{0,16})', column.replace(' ','') )
                    if search:
                        print( 'search:', search )
                        mainIban = self.checkIban( search.group(0), ibans )
                if len( mainIban ) == 1:
                    break;
                
        print( 'ibans:', ibans )
        print( 'mainIban:', mainIban )
        return mainIban

    def parseLine(self, line):
        print( line )

    def readFile(self, fileName):
        file = open( fileName )
        for line in file:
            self.parseLine( line )
        file.close()

    def __init__(self):
        config = ConfigObj('banks.config')
        
        banks = []

if __name__ == '__main__':
    b = Bank()

    home = expanduser("~")
    print( 'home:', home )

    mainIban = b.searchMainIban( 'test.csv' )

    # If mainIban contains more than one iban,
    # let the user select which one is the main iban
    # Otherwise we know the bank this csv belongs to

    if len( mainIban ) > 1:
        print( 'there\'s too many ibans' )
    elif len( mainIban ) == 0:
        print( 'No ibans found' )
    else:
        ibanType = mainIban[0][:8]
        
    
    
    b.readFile( 'test.csv' )
