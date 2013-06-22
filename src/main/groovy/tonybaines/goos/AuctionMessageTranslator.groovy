package tonybaines.goos

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

import static tonybaines.goos.AuctionEventListener.PriceSource.*

class AuctionMessageTranslator implements MessageListener {

  private final AuctionEventListener listener
  private final String sniperId

  AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
    this.sniperId = sniperId
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
        listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId))
        break
    }
  }

  static class AuctionEvent {
    private final def fields = [:]

    def type() { fields["Event"] }

    def currentPrice() { fields["CurrentPrice"] as Integer }

    def increment() { fields["Increment"] as Integer }

    def bidder() { fields["Bidder"] }

    def isFrom(String sniperId) {
      return sniperId.equals(bidder()) ? FromSniper : FromOtherBidder
    }


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
