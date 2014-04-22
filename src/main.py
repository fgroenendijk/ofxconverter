from config import Config
from input.bank import Bank
from output.ofx import Ofx
from model.bankStatement import BankStatement
from csvReader import CsvReader

from tkinter import Tk, Frame, Menu, filedialog, constants, ttk, Label, Checkbutton

class Example(Frame):
  
    def __init__(self, parent):
        Frame.__init__(self, parent, background="white")   
         
        self.parent = parent
        
        self.initUI()
    
    def initUI(self):
      
        self.parent.title("Simple")

        menubar = Menu(self.parent)
        self.parent.config(menu=menubar)
        
        fileMenu = Menu(menubar)
        fileMenu.add_command(label="Open", command=self.openFile)
        fileMenu.add_command(label="Exit", command=self.onExit, accelerator="Ctrl-e", underline=1 )
        menubar.add_cascade(label="File", menu=fileMenu)

        notebook = ttk.Notebook( self.parent )
        self.tabFiles = Frame( notebook )
        tabLogging = Frame( notebook, bg="green" )

        notebook.add( self.tabFiles, text="Files to process" )
        notebook.add( tabLogging, text="Logging" )

        notebook.pack(fill=constants.BOTH,expand=1,anchor=constants.N)

        button = ttk.Button( self.parent, command=self.openFile, text="Process" )
        button.pack(side=constants.RIGHT, padx=5, pady=5)

        self.tabFiles.grid_columnconfigure( 0, weight=1 )      

    def addFile(self,filename,ibanType):
        Label(self.tabFiles, text=filename,
                    borderwidth=3).grid(row=4,column=0,sticky=constants.W,padx=1)
        Label(self.tabFiles, text=ibanType,
                    borderwidth=3).grid(row=4,column=1,sticky=constants.E,padx=1)
        c = Checkbutton(self.tabFiles)
        c.grid(row=4,column=2)
        ttk.Separator(self.tabFiles).grid(row=5, sticky="ew", columnspan=3 )       


    def openFile(self):
        filename = filedialog.askopenfilename(parent=self.parent,
                                                filetypes=[('Csv files','.csv'),
                                                           ('All Files','.*')],
                                                title='Select the csv')
        print( filename )

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

        self.addFile(filename,ibanType)
        
        config = Config()
        fields = config.getCurrentBank( ibanType )

        bankStatement = BankStatement()

        bankStatement.account = ibans[0]

        csvReader = CsvReader(fields)
    
        csvReader.readFile( filename, bankStatement )

        ofx = Ofx()

        ofx.createXmlFile( filename, bankStatement )

    def onExit(self):
        quit()

if __name__ == '__main__':

    root = Tk()
    root.option_add('*tearOff', False)

    root.geometry("700x400+300+300")

    app = Example(root)
    root.mainloop()
    
    

    
