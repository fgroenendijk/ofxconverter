from configobj import ConfigObj

class Bank:

    def checkFile(self, fileName):
        file = open( fileName )
        for line in file:
            if ibanSearch = re.search( r'\w{2}\d{2}\w{4}\d{7}\w{0,16}', line ):
                for column in line:
                    if ibanSearch:
                        ibans = re.findall(r'\w{2}\d{2}\w{4}\d{7}\w{0,16}', column.replace(' ','') );  
        file.close()                

    def parseLine(self, line):
        print( line )

    def readFile(self, fileName):
        file = open( fileName )
        for line in file:
            parseLine( line )
        file.close()

    def __init__(self):
        config = ConfigObj('banks.config')
        
        banks = []

if __name__ == '__main__':
    b = Bank()

    b.checkFile( 'test.csv' )
    
    b.readFile( 'test.csv' );
