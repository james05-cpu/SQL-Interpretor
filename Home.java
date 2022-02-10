            import java.util.Vector;
            
            public class Home extends JFrame implements ActionListener {
                private JMenuBar menuBar;
                private JMenu Mode, more, file;
                private JMenuItem op, sav, nw, selPart, allQs, reset, exit, con, discon;
                 JTextArea editor, disp;
                 JTable table;
                private JPanel edpanel;
                ExecSQL execSQL = null;
                Scanner in = null;
                StringBuilder stringBuilder;
                String query;
                public class ExecSQL{
                    static String url;
                    static     String username;
                    static   String password;
                    Connection connection=null;
                    Statement stat=null;
                    DefaultTableModel defaultTableModel;
            
                    public  void parse(String sqlScript)
                    {
                        defaultTableModel=new DefaultTableModel();
                        try
                        {
                            try {
            
                                connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\ADMIN\\Documents\\TEST.accdb");
                                stat=connection.createStatement();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            String []lines=sqlScript.split(";");
                            for (String line:lines){
                                if (line.trim().endsWith(";")){
                                    line=line.trim();
                                    line=line.substring(0,line.length()-1);
                                }
                                if (line.equals("")){
                                    continue;
                                }
                                try {
                                    boolean hasResultSet=stat.execute(line);
                                    if (hasResultSet){
                                        showResultSet(stat);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
            
                        }
                        finally
                        {
                        }
                    }
            
                    public Connection getConnection(String url) throws SQLException, IOException
                    {
                        return DriverManager.getConnection(url);
                    }
                    public  void showResultSet(Statement stat) throws SQLException {
                        ResultSet result = stat.getResultSet();
                        ResultSetMetaData metaData = result.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        Vector vector = new Vector();
                        int j = 0;
                        for (int i = 1; i <= columnCount; i++) {
                            j++;
                        }
                        StringBuilder builder=new StringBuilder();
            
                        for (int i = 1; i <= columnCount; i++) {
                            defaultTableModel.addColumn(metaData.getColumnLabel(i));
                            builder.append(metaData.getColumnLabel(i)+" ");
                        }
                        String colstring=builder.toString().trim();
                        String[] cols=colstring.split(" ");
                        while (result.next()) {
                            for (String k : cols)
                                defaultTableModel.addRow(new String[]{result.getString(k)});
                        }
                        table.setModel(defaultTableModel);
                    }
                }
                public static void main(String[] args) {
                    new Home();
                }
            
                Home() {
                    execSQL = new ExecSQL();
                    menuBar = new JMenuBar();
                    Mode = new JMenu("Run");
                    more = new JMenu("More");
                    file = new JMenu("File");
                    con = createMenuItem("Connect");
                    discon = createMenuItem("Disconnect");
                    more.add(con);
                    more.add(discon);
                    nw = createMenuItem("New Script");
                    op = createMenuItem("Open Script");
                    sav = createMenuItem("Save as");
                    file.add(nw);
                    file.add(op);
                    file.add(sav);
                    selPart = createMenuItem("Selected");
                    allQs = createMenuItem("All");
                    reset = createMenuItem("Reset");
                    exit = createMenuItem("Exit");
                    editor = createTextArea();
            
                    JScrollPane pane = new JScrollPane(editor);
                    pane.setBounds(2, 2, 785, 265);
                    pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    editor.setLineWrap(true);
                    editor.setWrapStyleWord(true);
                    edpanel = createPanel();
                    disp = new JTextArea();
                    disp.setBounds(0, 290, 687, 250);
                    disp.setEditable(false);
                    disp.setLineWrap(true);
                    disp.setWrapStyleWord(true);
                    table=new JTable();
            
                    table.setModel(new DefaultTableModel(
                            new Object [][] {
            
                            },
                            new String [] {
            
                            }
                    ));
                    table.setPreferredScrollableViewportSize(new Dimension(700, 230));
                    table.setFillsViewportHeight(true);
            
                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setBounds(0, 350, 786, 230);
            
                    //add(new JScrollPane(table));
                   // defaultTableModel.addColumn("Name");
                    //defaultTableModel.addColumn("Node Display name");
                    //defaultTableModel.addColumn("LoopBack");
                    edpanel.add(pane);
                    Mode.add(selPart);
                    Mode.add(allQs);
                    more.add(reset);
                    more.add(exit);
                    menuBar.add(file);
                    menuBar.add(Mode);
                    menuBar.add(more);
                    add(edpanel);
                    add(scrollPane);
                    setSize(800, 650);
                    setLocationRelativeTo(this);
                    setJMenuBar(menuBar);
                    setTitle("        SQL INTERPRETER");
                    setLayout(null);
                    setResizable(false);
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setVisible(true);
                    validate();
            
                }
            
                JMenuItem createMenuItem(String text) {
                    JMenuItem menuItem = new JMenuItem(text);
                    menuItem.setMnemonic(text.charAt(0));
                    menuItem.addActionListener(this);
                    return menuItem;
                }
            
                JTextArea createTextArea() {
                    JTextArea textArea = new JTextArea();
                    textArea.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
                    textArea.setBounds(7, 10, 700, 220);
                    //textArea.setEditable(false);
                    return textArea;
                }
            
                JPanel createPanel() {
                    JPanel panel = new JPanel();
                    panel.setLayout(null);
                    panel.setForeground(Color.black);
                    panel.setBackground(new Color(58, 26, 65));
                    panel.setBounds(0, 0, 800, 287);
                    return panel;
                }
            
                public void Show(String results) {
                    editor.append(results);
                }
            
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == con) {
                        new CnnForm();
                    }
                    if (e.getSource() == discon) {
            
                    }
                    if (e.getSource() == nw) {
            
                    }
                    if (e.getSource() == op) {
                        stringBuilder=new StringBuilder();
                        JFileChooser opener = new JFileChooser();
                        opener.setFileFilter(new FileNameExtensionFilter("SQL only", "sql"));
                        opener.setMultiSelectionEnabled(true);
                        int resp = opener.showOpenDialog(null);
                        if (resp == JFileChooser.APPROVE_OPTION) {
                            File file = new File(opener.getSelectedFile().getAbsolutePath());
                            try {
                                in = new Scanner(file);
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                            if (file.isFile()) {
                                while (in.hasNextLine()) {
                                    String line = in.nextLine() + "\n";
                                    stringBuilder.append(line);
                                }
                                query = stringBuilder.toString();
                                editor.append(query);
                            }
                        }
                    }
                        if (e.getSource() == sav) {
                            JFileChooser chooser = new JFileChooser();
                            int response = chooser.showSaveDialog(null);
                            if (response == JFileChooser.APPROVE_OPTION) {
                                File fileout;
                                PrintWriter out = null;
                                fileout = new File(chooser.getSelectedFile().getAbsolutePath());
                                try {
                                    out = new PrintWriter(fileout);
                                    out.println(editor.getText());
            
                                } catch (IOException ex) {
                                } finally {
                                    out.close();
                                }
            
                            }
                        }
                        if (e.getSource() == selPart) {
                            if (editor.getSelectedText().equals("")) {
                                return;
                            } else {
                                execSQL.parse(editor.getSelectedText());
                            }
                        }
                        if (e.getSource() == allQs) {
                            if (editor.getText().equals("")) {
                                return;
                            } else {
                                execSQL.parse(editor.getText());
                            }
                        }
                        if (e.getSource() == reset) {
                            table.setModel(new DefaultTableModel());
                        }
                        if (e.getSource() == exit) {
                            System.exit(0);
                        }
                    }
                }
            
