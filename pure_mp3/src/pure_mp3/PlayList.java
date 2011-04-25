/*
 *  This file is part of pure.mp3.
 *
 *  pure.mp3 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  pure.mp3 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with pure.mp3.  If not, see <http://www.gnu.org/licenses/>.
 */
package pure_mp3;

import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * PlayList for the Player. Is not a JList but a JScrollPane
 * @author Martin Braun
*/
public class PlayList extends JScrollPane
{
	private static final long serialVersionUID = 2385007980763532219L;
    private JList list;
    private DropTarget dropTarget;
    private DefaultListModel model;
    private int current;
    
    /**
     * Basic Constructor
     */
    public PlayList()
    {
        super();
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Global.setPlayList(this);
        current = 0;        
        list = new JList();
        model = new DefaultListModel();
        
        list.setModel(model);
        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(new ListMoveTransferHandler());
        list.setBackground(Color.WHITE);
        list.setCellRenderer(new PlayListRenderer());
        
        list.addMouseListener(new MouseAdapter() 
        {
            public synchronized void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                {
                	try
                	{
                		playSelected(((JList)e.getSource()).locationToIndex(e.getPoint()));
                	}
                	catch(ArrayIndexOutOfBoundsException ex)
                	{
                	}
                }
             }
        });
        
        list.addKeyListener(new KeyListener() 
        {
        	public void keyPressed(KeyEvent event)
        	{
        		int selectAfter = 0;
        		if(event.getKeyCode() == KeyEvent.VK_DELETE)
        		{
        			int selected[] = list.getSelectedIndices();
        			if(selected.length >= 0)
        			{
        				selectAfter = selected[0];
        				if(selectAfter < 0)
            			{
            				selectAfter = 0;
            			}
        				model.trimToSize();
        			}
        			int killCount = 0;
        			for(int i = 0; i < selected.length; i++)
        			{
	        			if(selected[i] == current && Global.player.isPlaying())
	        			{
	        				prev();
	        			}
	        			if(selected[i] != -1)
	        			{
	        				model.remove(selected[i] - killCount);
	        				killCount++;	        				
	        			}	        			
        			}
        			list.setSelectedIndex(selectAfter);
        			if(list.isSelectionEmpty())
        			{
        				list.setSelectedIndex(0);
        			}
        			list.ensureIndexIsVisible(list.getSelectedIndex());
        			if(current > selected[selected.length-1])
        			{
        				current = current - selected[selected.length-1];
        			}
        		}
        	}  
        	
        	public void keyReleased(KeyEvent event)
        	{
        		//Do nothing.
        	}  
        	
        	public void keyTyped(KeyEvent event)
        	{
        		//Do nothing.
        	}
        });
        
        dropTarget = new DropTarget(list, new PlayListDropTargetListener());
        list.setDropTarget(dropTarget);

        list.setSelectedIndex(current);
        add(list);        
        setViewportView(list);
    }    
    
    //Methods that control the oder of Playback
    /**
     * chooses the next Song
     */
    public void next()
    {
        setCurrentAndDisplay(current+1);
    }
    
    /**
     * chooses the previous Song
     */
    public void prev()
    {
        setCurrentAndDisplay(current-1);
    }
    
    /**
     * chooses a random Song
     */
    public void random()
    {
    	Random random = new Random();
    	if(model.getSize() > 0)
    	{
    		setCurrentAndDisplay(random.nextInt(model.getSize())-1);
    	}
    }
    //End of the Playback Control Methods
    
    /**
     * Method used by a Internal Class for Playing the current Song    
     * @param index
     */
    private void playSelected(int index)
    {
        setCurrentAndDisplay(index);
        Global.player.stop();
        Global.player.playpause(false);
    }
        
    //Methods that check if the current Song is correct    
    /**
     * checks wheter the index of the current song isn't negative
     */
    public void checkCurrentNegative()
    {
    	if(current == -1 && model.getSize() > 0)
    	{
    		next();
    	}
    }    
    //End of checking Methods
    
    //Methods for adding or removing Songs
    /**
     * adds a Song at the end. After adding the List has to be validated
     * @param song
     */
    public void addSong(final Song song)
    {
    	SwingUtilities.invokeLater(new Runnable()
		{
		    public void run()
		    {
		    	System.out.println(song.getSource().toString());
		    	model.ensureCapacity(model.getSize()+1);
		    	if(song != null)
		    	{
		    		model.addElement(song);
		    	}
		    }
		});
    }
    
    /**
     * Adds Song at the specified position. After adding, the list has to be validated
     * @param song
     * @param position
     */
    public void addSongAt(final Song song, final int position)
    {
    	SwingUtilities.invokeLater(new Runnable()
		{
		    public void run()
		    {
		    	System.out.println(song.getSource().toString() + " at " + position);
		    	model.ensureCapacity(model.getSize()+1);
		    	if(song != null)
		    	{
		    		model.add(position,song);
		    	}
		    }
		});
    }
    
    /**
     * Clears the PlayList from all Songs
     */
    public void removeAllElements()
    {
    	SwingUtilities.invokeLater(new Runnable()
    	{
    		public void run()
    		{
    			model.removeAllElements();
    	    	model.trimToSize();
    	    	current = -1; 
    	    	repaint(getViewport().getViewRect());
    		}
    	});    	
    }   
    //End of the Adding/Removing Methods
    
    //The Setter Methods:
    /**
     * @param xCurrent the index of the Song that the currentSong will be set to
     */
    public void setCurrent(int xCurrent)
    {
    	if(Global.player.isPlaying())
    	{
    		current = xCurrent;
    	}
    }
    
    /**
     * Sets wheter a DropTarget has to listen
     * @param isActive
     */
    public void setDropTargetActive(boolean isActive)
    {
    	if(isActive)
    	{
    		list.setDropTarget(dropTarget);
    	}
    	else
    	{
    		list.setDropTarget(null);
    	}
    }
    
    /**
     * sets the chosen Song
     * @param xCurrent
     */
    public void setCurrentAndDisplay(final int xCurrent)
    {
    	if(model.getSize() > 0)
    	{
	    	if(xCurrent < 0)
	        {
	            current = model.getSize()-1;
	        }
	        else if(xCurrent+1 <= model.getSize() && xCurrent > 0)
	        {
	            current = xCurrent;
	        }
	        else
	        {
	            current = 0;
	        }
	    	SwingUtilities.invokeLater(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(model.getSize() > 0)
	    			{
				        if(model.get(current)!=null)
				        {
				            list.setSelectedIndex(current);
				            list.ensureIndexIsVisible(current);	
				        }
				        invalidate();
				        validate();
		    			repaint(getViewport().getViewRect());
	    			}
	    		}
	    	});   
    	}
    	
    }
    //End of the Setter Methods
    
    //The Getter Methods:
    /**
     * @return the JList in PlayList
     */
    public JList getList()
    {
    	return list;
    }
    
    /**
     * @return the Model of the JList
     */
    public DefaultListModel getModel()
    {
    	return model;
    }
    
    /**
     * @return the current Song
     */
    public Song getCurrentSong()
    {
    	if(model.getSize() > 0 && current > 0)
    	{
    		return (Song)model.get(current);
    	}
    	return null;
    }
    
    /**
     * returns the size of the PlayList, 
     * not named size() because of possibility of confusion with getSize() 
     * from JComponent
     * @return number of songs in the PlayList
     */
    public int getNumberOfSongs()
    {
    	return model.getSize();
    }
    
    /**
     * returns the index of the chosen Song
     * @return the index of the current Song
     */
    public int getCurrent()
    {
        return current;
    }
    //End of the Getter Methods
}
