package org.nextrtc.signalingserver.cases;

import org.nextrtc.signalingserver.api.NextRTCEventBus;
import org.nextrtc.signalingserver.domain.Conversation;
import org.nextrtc.signalingserver.domain.InternalMessage;
import org.nextrtc.signalingserver.domain.Member;
import org.nextrtc.signalingserver.domain.Signals;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static org.nextrtc.signalingserver.api.NextRTCEvents.TEXT;

@Component(Signals.TEXT_HANDLER)
public class TextMessage implements SignalHandler {

    private NextRTCEventBus eventBus;

    @Inject
    public TextMessage(NextRTCEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void execute(InternalMessage message) {
        Member from = message.getFrom();
        if (message.getTo() == null && from.getConversation().isPresent()) {
            Conversation conversation = from.getConversation().get();
            conversation.broadcast(from, message);
            eventBus.post(TEXT.basedOn(message));
        } else if (from.hasSameConversation(message.getTo())) {
            message.send();
            eventBus.post(TEXT.basedOn(message));
        }

    }
}
