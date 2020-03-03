import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FrameListener implements WindowListener, KeyListener {
    private final DistributedChat parent;

    public FrameListener(DistributedChat parentArg) {
        parent = parentArg;
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        parent.quit();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyInput) {
    	parent.keyTyped(keyInput);
    }

    @Override
    public void keyPressed(KeyEvent keyInput) {

    }

    @Override
    public void keyReleased(KeyEvent keyInput) {

    }

}