package vehicle;

import attachments.Attachment;

public interface ISnowPlow {
    boolean changeAttachment(Attachment a);
    boolean buyAttachment(Attachment newAttachment);
    void refillAttachment();
}
