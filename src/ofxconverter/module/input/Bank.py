from configobj import ConfigObj

class Bank:

    def checkFile( fileName ):
        

    def parseLine(self, line):
        print( line )

    def readFile( fileName ):
        file = open( fileName )
        for line in file:
            parseLine( line )
        file.close()

    def __init__(self):
        config = ConfigObj('banks.config')
        
        banks = []

if __name__ == '__main__':
    b = Bank()
    b.readFile( '' );
