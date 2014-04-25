from config import Config
from input.bank import Bank
from output.ofx import Ofx
from model.bankStatement import BankStatement
from csvReader import CsvReader

from tkinter import Tk, Frame, Menu, filedialog, constants, ttk, Text
from tkinter import Label, Checkbutton, IntVar, Scrollbar, Canvas
import tkinter._fix # needed for cx_freeze

class OfxConverter(Frame):
  
    def __init__(self, parent):
        Frame.__init__(self, parent, background="white")   
         
        self.parent = parent
        self.guiMap = []
        self.row = 0
        
        self.initUI()

    def writeLog(self,*msgs):
        self.log['state'] = 'normal'
        if self.log.index('end-1c')!='1.0':
            self.log.insert('end', '\n')
        for msg in msgs:
            print( msg )
            self.log.insert('end', msg)
        self.log['state'] = 'disabled'
    
    def initUI(self):
      
        self.parent.title("Simple")

        menubar = Menu(self.parent)
        self.parent.config(menu=menubar)
        
        fileMenu = Menu(menubar)
        fileMenu.add_command(label="Open", command=self.openFile)
        fileMenu.add_command(label="Exit", command=self.onExit, accelerator="Ctrl-e", underline=1 )
        menubar.add_cascade(label="File", menu=fileMenu)

        notebook = ttk.Notebook( self.parent )
        tabFilesMain = Frame( notebook )

        self.tabFiles = Frame( tabFilesMain )
        self.tabFiles.pack(fill=constants.BOTH)

        button = ttk.Button( tabFilesMain, command=self.parseFile, text="Process" )
        button.pack(side="bottom", fill=constants.X, padx=5, pady=5)
        
        tabLogging = Canvas( notebook )
        tabLogging.grid_rowconfigure(0, weight=1)
        tabLogging.grid_columnconfigure(0, weight=1)

        self.log = Text(tabLogging, wrap='word')
        self.log.grid(row=0,column=0,sticky='news')

        hScroll = Scrollbar(tabLogging, orient=constants.HORIZONTAL, command=self.log.xview)
        hScroll.grid(row=1, column=0, sticky='we')
        vScroll = Scrollbar(tabLogging, orient=constants.VERTICAL, command=self.log.yview)
        vScroll.grid(row=0, column=1, sticky='ns')
        self.log.configure(xscrollcommand=hScroll.set, yscrollcommand=vScroll.set)

        notebook.add( tabFilesMain, text="Files to process" )
        notebook.add( tabLogging, text="Logging" )

        notebook.pack(fill=constants.BOTH,expand=1,anchor=constants.N)

        self.tabFiles.grid_columnconfigure( 0, weight=1 ) 

    def addFile(self,filename,ibans):
        if filename != "" and len(ibans) > 0:
            Label(self.tabFiles, text=filename,
                    borderwidth=3).grid(row=self.row,column=0,sticky=constants.W,padx=1)
            
            ibanList = []
            for iban in ibans:
                ibanList.append( iban[:8] )
            combo = ttk.Combobox(self.tabFiles,values=ibanList)
            combo.current(0)
            if len(ibanList) == 1:
                combo.configure(state=constants.DISABLED)
            combo.grid(row=self.row,column=1,sticky=constants.E,padx=1)
            
            state = IntVar()
            c = Checkbutton(self.tabFiles,variable=state)
            c.grid(row=self.row,column=2)
            self.row += 1
            ttk.Separator(self.tabFiles).grid(row=self.row, sticky="ew", columnspan=3 )
            self.row += 1
            self.guiMap.append( [ filename, ibans, combo, c, state ] )

    def openFile(self):
        filename = filedialog.askopenfilename(parent=self.parent,
                                                filetypes=[('Csv files','.csv'),
                                                           ('All Files','.*')],
                                                title='Select the csv')
        if filename != "":
            self.writeLog( 'File added: ', filename )

            bank = Bank()

            ibans = bank.searchMainIban( filename )

            # If mainIban contains more than one iban,
            # let the user select which one is the main iban
            # Otherwise we know the bank this csv belongs to

            if len( ibans ) > 1:
                self.writeLog( 'there\'s too many ibans, please select one from the list' )
            elif len( ibans ) == 0:
                self.writeLog( 'No ibans found, is the file correct?' )
            else:
                self.writeLog( 'Found iban: ', ibans[0][:8] )
                ibanType = ibans[0][:8]

            self.addFile(filename,ibans)


    def parseFile(self):

        for guiLine in self.guiMap:
            ( filename, ibans, combo, checkButton, state ) = guiLine

            if state.get():
                ibanType = combo.get()
                
                config = Config()
                fields = config.getCurrentBank( ibanType )

                bankStatement = BankStatement()

                bankStatement.account = ibans[0]

                csvReader = CsvReader(fields)
            
                csvReader.readFile( filename, bankStatement )

                ofx = Ofx()

                ofx.createXmlFile( filename, bankStatement )
                checkButton.configure(state=constants.DISABLED)

    def onExit(self):
        quit()

if __name__ == '__main__':

    root = Tk()
    root.option_add('*tearOff', False)

    root.geometry("700x400+300+300")

    app = OfxConverter(root)
    root.mainloop()
    
    

    
