package tonybaines.goos.testsupport

import groovy.util.logging.Log
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.packet.Message

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

import static tonybaines.goos.app.Main.*

@Log
class FakeAuctionServer {
  public static final String XMPP_HOSTNAME = "localhost"
  private static final AUCTION_PASSWORD = "auction"
  final String itemId
  private final XMPPConnection connection
  private Chat currentChat
  private final SingleMessageListener messageListener = new SingleMessageListener()

  public FakeAuctionServer(String itemId) {
    this.itemId = itemId
    this.connection = new XMPPConnection(XMPP_HOSTNAME)
  }

  public void startSellingItem() throws XMPPException {
    use(XMPPConnectionHelper) { // Scoped mix-in to make the connection setup more literate
      connection.connectAs(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
      connection.registerListener { chat, createdLocally ->
        currentChat = chat
        chat.addMessageListener(messageListener)
      }
    }
  }

  public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
    messageListener.receivesAMessage()
  }

  public void announceClosed() throws XMPPException {
    currentChat.sendMessage(new Message())
  }

  public void stop() {
    connection.disconnect()
  }

  public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1)

    public void processMessage(Chat chat, Message message) {
      messages.add(message)
    }

    public void receivesAMessage() throws InterruptedException {
      assert messages.poll(5, TimeUnit.SECONDS) != null
    }
  }

  @Category(XMPPConnection)
  public static class XMPPConnectionHelper {
    static void connectAs(XMPPConnection self, username, password, resource) {
      self.connect()
      self.login(username, password, resource)
    }

    static void registerListener(XMPPConnection self, Closure c) {
      self.getChatManager().addChatListener(
        new ChatManagerListener() {
          public void chatCreated(Chat chat, boolean createdLocally) {
            c.call(chat, createdLocally)
          }
        })
    }
  }

}
