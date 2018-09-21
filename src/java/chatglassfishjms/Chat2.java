package chatglassfishjms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class Chat2 extends javax.swing.JFrame implements MessageListener {

    private TopicConnectionFactory connectionFactory;
    private TopicConnection connection;
    private TopicSession subSession;
    private TopicSession pubSession;
    private TopicPublisher publisher;
    private TopicSubscriber subscriber;
    
    
    public Chat2() {
        initComponents();
        this.setTitle("Koko");
        try {
            InitialContext initContext = new InitialContext();
            connectionFactory = (TopicConnectionFactory)initContext.lookup("ChatConnection");
            connection = connectionFactory.createTopicConnection();
            pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) initContext.lookup("ChatQueue");
            publisher = pubSession.createPublisher(topic);
            subscriber = subSession.createSubscriber(topic);
            subscriber.setMessageListener(this);
            Set(subSession, pubSession, connection, publisher, subscriber);
            connection.start();
            
            tfInput.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = tfInput.getText().trim();
                    if(!text.equalsIgnoreCase("exit")){
                        writeMessage("["+getTitle()+"]: "+text);
                        System.out.println("If: ["+getTitle()+"]: "+text);
                        tfInput.setText("");
                    }else{
                        Close();
                        System.exit(0);
                    }
                    
                }
                
            });
            
             btnSend.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = tfInput.getText().trim();
                    if(!text.equalsIgnoreCase("exit")){
                        writeMessage("["+getTitle()+"]: "+text);
                        System.out.println("If: ["+getTitle()+"]: "+text);
                        tfInput.setText("");
                    }else{
                        Close();
                        System.exit(0);
                    }
                    
                }
                
            });
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
    }
    
    private void Set(TopicSession subSession, TopicSession pubSession, TopicConnection connection, 
            TopicPublisher publisher, TopicSubscriber subscriber){
        this.connection = connection;
        this.subSession = subSession;
        this.pubSession = pubSession;
        this.publisher = publisher;
        this.subscriber = subscriber;
    }
    
    private void Close(){
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writeMessage(String text){
        
        try {
            TextMessage message = pubSession.createTextMessage(text);
            publisher.publish(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taContent = new javax.swing.JTextArea();
        tfInput = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taContent.setColumns(20);
        taContent.setRows(5);
        jScrollPane1.setViewportView(taContent);

        btnSend.setText("Enviar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfInput, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfInput)
                    .addComponent(btnSend))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taContent;
    private javax.swing.JTextField tfInput;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onMessage(Message message) {
       try{
            TextMessage textMessage = (TextMessage)message;
            System.out.println("onMessage_:"+textMessage.getText());
            taContent.append(textMessage.getText()+"\n");
	}catch(Exception e){
		e.printStackTrace();
	}
    }
    
}
