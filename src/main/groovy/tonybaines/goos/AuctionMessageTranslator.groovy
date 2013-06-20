package tonybaines.goos

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

class AuctionMessageTranslator implements MessageListener {

  private final AuctionEventListener listener

  AuctionMessageTranslator(AuctionEventListener listener) {
    this.listener = listener
  }

  @Override
  public void processMessage(Chat chat, Message message) {
    AuctionEvent event = AuctionEvent.from(message.body);

    switch (event.type()) {
      case "CLOSE":
        listener.auctionClosed()
        break
      case "PRICE":
        listener.currentPrice(event.currentPrice(), event.increment())
        break
    }
  }

  static class AuctionEvent {
    private final def fields = [:]

    def type() { fields["Event"] }

    def currentPrice() { fields["CurrentPrice"] as Integer }

    def increment() { fields["Increment"] as Integer }

    private void addField(field) {
      def (k, v) = field.split(":")
      fields[k.trim()] = v.trim()
    }

    static AuctionEvent from(messageBody) {
      def event = new AuctionEvent()
      fieldsIn(messageBody).each { event.addField(it) }

      return event
    }

    static def fieldsIn(messageBody) { messageBody.split(";") }
  }


}
