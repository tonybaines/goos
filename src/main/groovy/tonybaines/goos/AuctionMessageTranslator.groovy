package tonybaines.goos

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

class AuctionMessageTranslator implements MessageListener{

  private final AuctionEventListener listener

  AuctionMessageTranslator(AuctionEventListener listener) {
    this.listener = listener
  }

  @Override
  public void processMessage(Chat chat, Message message) {
    def event = unpackEventFrom(message)
    String type = event["Event"]
    if ("CLOSE".equals(type)) {
      listener.auctionClosed()
    } else if ("PRICE".equals(type)) {
      listener.currentPrice(event["CurrentPrice"] as Integer, event["Increment"] as Integer)
    }
  }
  private def unpackEventFrom(Message message) {
    return message.body.split(';').collectEntries {
      def (k,v) = it.split(":")
      [k.trim(), v.trim()]
    }
  }

}
