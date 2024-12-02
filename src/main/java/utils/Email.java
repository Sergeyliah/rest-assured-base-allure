package utils;

import com.sun.mail.imap.IMAPFolder;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Email {

    private static final String EMAIL_ID = "sergeodtest@gmail.com";
    private static final String HOST = "imap.gmail.com";
    private static final String EMAIL_PASS = "autotesttest12345";


    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.put("mail.store.protocol", "imaps");
        PROPERTIES.put("mail.imap.host", HOST);
        PROPERTIES.put("mail.imap.port", "993");
        PROPERTIES.put("mail.imap.connectiontimeout", "12000");
        PROPERTIES.put("mail.imap.timeout", "12000");
    }

    public void clearEmail() {
        Session session = session();
        Store store = store(session);
        //Connect to mail, get store and inbox
        Folder inbox = null;
        try {
            connectToStore(store);
            inbox = getInboxFolder(store);
            openInBoxFolderWithRWPermissions(inbox);
            // Make unseen flag for inbox mails
            Message[] unseenMessages = messages(inbox, false);
            Message[] seenMessages = messages(inbox, true);

            int unseenMsgLength = unseenMessages.length;
            int seenMsgLength = seenMessages.length;
            // Delete all previous mails
            log.info("Crap messages unseen = {} " +
                    "and seen = {}", unseenMsgLength, seenMsgLength);
            deleteEmails(inbox, unseenMessages);
            deleteEmails(inbox, seenMessages);
            inbox.expunge();
            // check unseen msg again to be sure
            unseenMessages = messages(inbox, false);
            seenMessages = messages(inbox, true);
            unseenMsgLength = unseenMessages.length;
            seenMsgLength = seenMessages.length;
            log.info("Messages after clearing: unseen: {} and seen: {}",
                    unseenMsgLength, seenMsgLength);
        } catch (MessagingException e) {
            log.error("{}", e.getMessage());
        }
        try {
            assert inbox != null;
            inbox.close();
            log.info("Inbox closed.");
            store.close();
            log.info("Store closed.");
        } catch (MessagingException e){
            log.info("{}", e.getMessage());
        }
    }

    public String getConfirmationLink(String messageContains, Pattern pattern) {
        final String[] messageContent = new String[1];
        final String[] confirmLink = {""};
        int msgCount;

        Session session = session();
        Store store = store(session);
        Folder inbox;
        try {
            connectToStore(store);
            inbox = getInboxFolder(store);
            openInBoxFolderWithRWPermissions(inbox);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        try {
            do {
                log.info("Do cycle");
                // set how many messages are in the inbox when the array is created
                msgCount = inbox.getMessageCount();
                log.info("msg count: {}", msgCount);
                // Fetch unseen messages from inbox folder
                Message[] unseenMsg = messages(inbox, false);
                log.info("unseen messages: {}", Arrays.toString(unseenMsg));
                Message[] seenMsg = messages(inbox, true);
                log.info("seen messages: {}", Arrays.toString(seenMsg));
                for (Message msg : unseenMsg) {
                    messageContent[0] = msg.getContent().toString();
                    confirmLink[0] = parseMailForConfirmationLink(msg, messageContains, pattern);
                }
                // if a new message came in while reading the messages start the loop over and get all unread messages
            } while (inbox.getMessageCount() != msgCount);
            inbox.addMessageCountListener(new MessageCountAdapter() {
                @Override
                public void messagesAdded(MessageCountEvent event) {
                    log.info("Verify msg came when run DO cycle");
                    Message[] messages = event.getMessages();
                    for (Message msg : messages) {
                        try {
                            messageContent[0] = msg.getContent().toString();
                            confirmLink[0] = parseMailForConfirmationLink(msg, messageContains, pattern);
                        } catch (IOException | MessagingException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            // wait for new messages
            if (confirmLink.equals("")) {
                while (inbox.isOpen()) {
                    log.info("No new messages. Start scheduler and wait for mail.");
                    // every 10 seconds poke the server with an inbox.getMessages() to keep the connection active/open
                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    Folder finalInbox = inbox;
                    final Runnable pokeInbox = () -> {
                        try {
                            if (finalInbox.getMessageCount() >= 1) {
                                Message[] messages = messages(finalInbox, false);
                                for (Message msg : messages) {
                                    confirmLink[0] = parseMailForConfirmationLink(msg, messageContains, pattern);
                                    break;
                                }
                            }
                        } catch (MessagingException | IOException ignored) {
                        }
                    };
                    scheduler.schedule(pokeInbox, 10, TimeUnit.SECONDS);
                    ((IMAPFolder) inbox).idle();
                    if (!confirmLink[0].equals("")) {
                        inbox.close();
                        store.close();
                        break;
                    }
                }
            }
            log.info("{}", Arrays.toString(confirmLink));
        } catch (FolderClosedException | IOException e){
            e.printStackTrace();
            log.info("Error connection was dropped");
            try {
                store.close();
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            getConfirmationLink(messageContains, pattern);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } finally {
            if (!confirmLink[0].equals("")){
                try {
                    store.close();
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return confirmLink[0];
    }
    private String parseMailForConfirmationLink(Message msg, String messageContains,
                                                Pattern pattern) throws MessagingException, IOException {
        String confirmationLink = "";
        String messageContent = msg.getContent().toString();
        log.info("msgContent: {}", messageContent);
        if (messageContent.contains(messageContains)){
            // Reg expression to find and take confirmation URL from mail
            Matcher matcher = pattern.matcher(messageContent);
            while (matcher.find()){
                confirmationLink = matcher.group();
                log.info("Confirmation link: {}", confirmationLink);
            }
        }
        return confirmationLink;
    }
    //*********************************************
    public Message[] getMassage() throws MessagingException {
        Session session = session();
        Store store = store(session);
        Folder inbox;
        try {
            connectToStore(store);
            inbox = getInboxFolder(store);
            openInBoxFolderWithRWPermissions(inbox);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return messages(inbox, true);
    }
    //*********************************************

    private Session session() {
        return Session.getDefaultInstance(PROPERTIES, null);
    }

    private Store store(Session session){
        Store store = null;
        try {
            store = session.getStore("imaps");
        } catch (NoSuchProviderException e){
            log.error("{}", e.getMessage());
        }
        log.debug("Connection initiated -> got Store");
        return store;
    }

    private void connectToStore(Store store) throws MessagingException {
        assert store != null;
        store.connect(HOST, EMAIL_ID, EMAIL_PASS);
        log.info("CONNECTED");
    }

    private Folder getInboxFolder(Store store) throws MessagingException {
        Folder inbox = store.getFolder("inbox");
        log.info("Connected. Got inbox\n" +
                "Inbox delivered to Clear Email method");
        return inbox;
    }

    private void openInBoxFolderWithRWPermissions(Folder folder) throws MessagingException {
        folder.open(Folder.READ_WRITE);
        log.info("Inbox opened with R/W permissions");
    }

    private Message[] messages(Folder folder, boolean flag) throws MessagingException {
        FlagTerm flagTerm = new FlagTerm(
                new Flags(Flags.Flag.SEEN), flag);
        Message[] messages = folder.search(flagTerm);
        if (messages != null) {
            return folder.search(flagTerm);
        } else {
            return new Message[0];
        }
    }

    private void deleteEmails(Folder folder, Message[] messages) throws MessagingException {
        folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
    }
}