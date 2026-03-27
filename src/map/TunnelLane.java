package map;

import attachments.Attachment;

public class TunnelLane extends Lane {

    public void snowFall(int snow) {}

    public void cleanWithHead(Attachment head) {}

    @Override
    public void cleanWithDragon() {}

    @Override
    public void cleanWithSaltVomitter(int timestamp) {}

    @Override
    public void cleanWithSweeper() {}

    @Override
    public void cleanWithIceBreaker() {}

    @Override
    public void cleanWithVomittingHead() {}
}
