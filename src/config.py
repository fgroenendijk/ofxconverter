import sys
from configobj import ConfigObj
from os.path import expanduser
from os.path import join
from os.path import isdir
from os.path import isfile
from os.path import exists
from os import makedirs
from os import path
import errno
import pygit2

class Config:

    # Numeric order config file
    # 1 interestDate
    # 2 date
    # 3 amount
    # 4 memo
    # 5 description
    # 6 account
    # 7 type
    # 8 debit/credit
    # 9 currency

    def getCurrencies(self):
        cfg = ConfigObj( self.configFile, encoding="utf8", default_encoding="utf8" )

        currencies = {}

        for currencyName, currencyIso in cfg['currencies'].items():
            currencies[ currencyName ] = currencyIso

        return currencies
        

    def addToConfig(self,fields,section='banks'):

#        cfg = ConfigObj( open( self.configFile, 'rt', encoding='utf-8' ) )
        cfg = ConfigObj( self.configFile, encoding='utf-8' )
        cfgValue = []

        if 'main account' in fields:
            key = fields['main account'][:8] 
        
            if 'interestDate' in fields:
                cfgValue.append( str(fields['interestDate']) )
            else:
                cfgValue.append( '-1' )
                
            if 'date' in fields:
                cfgValue.append( str(fields['date']) )
            else:
                cfgValue.append( '-1' )
                
            if 'amount' in fields:
                cfgValue.append( str(fields['amount']) )
            else:
                cfgValue.append( '-1' )
                
            if 'memo' in fields:
                cfgValue.append( str(fields['memo']) )
            else:
                cfgValue.append( '-1' )
                
            if 'description' in fields:
                cfgValue.append( str(fields['description']) )
            else:
                cfgValue.append( '-1' )
                
            if 'account' in fields:
                cfgValue.append( str(fields['account']) )
            else:
                cfgValue.append( '-1' )
                
            if 'type' in fields:
                cfgValue.append( str(fields['type']) )
            else:
                cfgValue.append( '-1' )
                
            if 'credit/debit' in fields:
                cfgValue.append( str(fields['credit/debit']) )        
            else:
                cfgValue.append( '-1' )
            if 'currency' in fields:
                cfgValue.append( fields['currency'] )        
            else:
                cfgValue.append( '-1' )

        cfg[ section ] = {}
        cfg[ section ][ key ] = cfgValue
        print(( "trying to write", key, "with value", cfgValue, "in", section ))
        cfg.write()

        return [ key, cfgValue ]
                

    def checkForConfig(self):
        home = expanduser("~")

        configDir = join( home, '.ofxconverter' )

        if sys.version_info >= (3,0):
            makedirs( configDir, exist_ok=True )
        else:
            try:
                makedirs(configDir)
            except OSError as exc:  # Python >2.5
                if exc.errno == errno.EEXIST and path.isdir(configDir):
                    pass
            else:
                raise

        repo_url = 'git://git@github.com/weirdall/ofxconverter'

        self.configFile = join( configDir, 'banks.config' )
        
        if not exists( self.configFile ) or not isfile( self.configFile ):
            try:
                repo = pygit2.clone_repository(repo_url, configDir, checkout_branch='config')
            except KeyError:
                print(( "Repository at", configDir, "already initialized" ))
        elif isfile( self.configFile ):
            repo = pygit2.Repository( configDir )
            repo.checkout()
                

    def getCurrentBank(self, bank):
        cfg = ConfigObj( self.configFile, encoding="utf8", default_encoding="utf8" )  
        fields = []
        for cfgBank, cfgBankValue in cfg['banks'].items():
            if cfgBank == bank:
                for fieldNumber in cfgBankValue:
                    if not 'interestDate' in (x[0] for x in fields):
                        fields.append( [ 'interestDate', fieldNumber ] )
                    elif not 'date' in (x[0] for x in fields):
                        fields.append( [ 'date', fieldNumber ] )
                    elif not 'amount' in (x[0] for x in fields):
                        fields.append( [ 'amount', fieldNumber ] )
                    elif not 'memo' in (x[0] for x in fields):
                        fields.append( [ 'memo', fieldNumber ] )
                    elif not 'description' in (x[0] for x in fields):
                        fields.append( [ 'description', fieldNumber ] )
                    elif not 'account' in (x[0] for x in fields):
                        fields.append( [ 'account', fieldNumber ] )
                    elif not 'type' in (x[0] for x in fields):
                        fields.append( [ 'type', fieldNumber ] )
                    elif not 'credit/debit' in (x[0] for x in fields):
                        fields.append( [ 'credit/debit', fieldNumber ] )
                    elif not 'currency' in (x[0] for x in fields):
                        fields.append( [ 'currency', fieldNumber ] )

        return fields
                        
    def __init__(self):
        self.checkForConfig()
