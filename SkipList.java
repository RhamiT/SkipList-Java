import java.util.ArrayList;
// import java.util.Random; This was a file provided by the Professor 

// Rhami Thrower
// Spring 2023
class Node<type>
{
    private ArrayList<Node<type>> stack;
    private int stackHeight;
    private type  value; 

    Node(int  height)
    {
        stackHeight = height;
        stack = new ArrayList<Node<type>>(height); //Node<type>[height];
        for(int i = 0; i < height; i++)
        {
            stack.add(null);
        }
        //for(int i = height - 1; i >=0; i--)
        //{
        //    stack.set(i, null); //= null;
        //}
    }
    Node(type  data, int  height)
    {
        stackHeight = height;
        this.value = data;
        stack = new ArrayList<Node<type>>(height); //Node<type>[height];

        for(int i = 0; i < height; i++)
        {
            stack.add(null);
        }
        //for(int i = height - 1; i >=0; i--)
        //{
        //    stack.set(i, null); //= null;
        //}
    }

    public type value()
    {
        return value;
    }
    public int height()
    {
        return stackHeight;
    }

    //it is assumed that this function is always given a valid lvl
    public Node<type> next(int level)
    {
        if(level < 0 || level >= stackHeight) return null;
        return stack.get(level); // stack[level];
    }

    // this function assumes a valid Node<type> and lvl is given
    public void setNext(int lvl, Node<type> newNode)
    {
        stack.set(lvl, newNode); //  = newNode;
    }

    //Functions to change a Nodes Height
    public void grow()
    {
        
       // Node<type> [] newStack;
       // newStack = new ArrayList<Node<type>>(this.height() + 1); //Node<type>[this.height() + 1];
       // newStack[this.height()] = null;
       // for(int i = this.height() - 1; i >= 0; i--)
        //{
            //newStack[i] = stack[i];
       // }
       // stack = newStack;
       stack.add(null);
       stackHeight++;
    }
    public boolean maybeGrow()
    {
        //Node<type> [] newStack;
        if(Math.random() >= 0.5) // time to grow
        {
            //newStack = new Node<type>[this.height() + 1];
            //newStack[this.height()] = null;
            //for(int i = this.height() - 1; i >= 0; i--)
            //{
            //    newStack[i] = stack[i];
            //}
            //stack = newStack;
            stack.add(null);
            stackHeight++;
            return true;
        }
        return false;
    }
    public void trim(int height)
    { 
        //Node<type> [] newStack;
        //newStack = new Node<type>[height];
        //for(int i = height - 1; i >= 0; i--)
        //{
        //    newStack[i] = stack[i];
        //}
        //stack = newStack;
        for(int i = 0; i < stackHeight - height; i++)
        {
            stack.remove(stackHeight - i - 1);
        }
        stackHeight -= stackHeight - height;
    }
}

public class SkipList<type extends Comparable<type>>
{
    private Node<type> head;
    private int size = 0;


    SkipList()
    {
        head = new Node<type>(1);
    }
    SkipList(int height)
    {
        if(height < 1)
        height = 1;
        head = new Node<type>(height);
    }

    public int size()
    {
        return size;
    }
    public int height()
    {
        return head.height();
    }
    
    public Node<type> head()
    {
        return this.head;
    }

    //how to check for the size
    public void insert(type data) 
    {
        //maintanence 
        size++;
        growSize();
        //variables
        int depth = 1; 
        int listHeight = this.height();
        int newHeight = randHeight(listHeight);
        int nxt;
        Node<type> newNode = new Node<type>(data, newHeight);
        Node<type> temp = head;

        //insert
        while((listHeight - depth) >= 0)
        {
            nxt = listHeight - depth;
            //check wheather nxt is null or not
            if(temp.next(nxt) != null)
            {
                if(temp.next(nxt).value().compareTo(data) < 0)
                {
                    temp = temp.next(nxt);
                }
                else // nxt >= data
                {
                    if(nxt <= newHeight - 1)
                    {
                        newNode.setNext(nxt, temp.next(nxt));
                        temp.setNext(nxt, newNode);
                    }
                    depth++;
                }
            }
            else // nxt is null
            {
                // check if depth is equal to new Node<type> height or less
                if(nxt <= newHeight - 1)
                {
                    newNode.setNext(nxt, temp.next(nxt));
                    temp.setNext(nxt, newNode);
                }
                depth++;
            }
        }

    }

    public void insert(type data, int height)
    {
        //maintanence 
        size++;
        growSize();
        //variables
        int depth = 1; 
        int listHeight = this.height();
        int newHeight = height;
        int nxt;
        Node<type> newNode = new Node<type>(data, newHeight);
        Node<type> temp = head;
        
        //insert
        while((listHeight - depth) >= 0)
        {
            nxt = listHeight - depth;
            //check wheather nxt is null or not
            if(temp.next(nxt) != null)
            {
                if(temp.next(nxt).value().compareTo(data) < 0) // traverse down list
                {
                    temp = temp.next(nxt);
                }
                else // nxt >= data
                {
                    if(nxt <= newHeight - 1)
                    {
                        newNode.setNext(nxt, temp.next(nxt));
                        temp.setNext(nxt, newNode);
                    }
                    depth++;
                }
            }
            else // nxt is null
            {
                // check if depth is equal to new Node<type> height or less
                if(nxt <= newHeight - 1)
                {
                    newNode.setNext(nxt, temp.next(nxt));
                    temp.setNext(nxt, newNode);
                }
                depth++;
            }
        }
    }

    public void delete(type data)
    {
        // maintenance 
        if(contains(data))
            size--;
        else    
            return;
        shrinkSize();
        // variables
        int depth = 1;
        int listHeight = this.height();
        int nxt;
        int deleteHeight = Integer.MAX_VALUE;
        Node<type> temp = head;
        ArrayList<Node<type>> crumbs = new ArrayList<Node<type>>(listHeight);

        // delete
        while((listHeight - depth) >= 0)
        {
            nxt = listHeight - depth;
            //check for null
            if(temp.next(nxt) != null)
            {
                if(temp.next(nxt).value().compareTo(data) == 0)
                {
                    //prevents deltion of later node instead of first
                    if(deleteHeight > temp.next(nxt).height()) 
                    {
                        crumbs.clear();
                        deleteHeight = temp.next(nxt).height();
                    }
                    crumbs.add(0,temp);
                    depth++;
                }
                else if(temp.next(nxt).value().compareTo(data) > 0)
                {
                    depth++;

                }
                else // nxt < data
                {
                    temp = temp.next(nxt);
                }

            }
            else// nxt is null
            {
                depth++;
            }
        }

        // feed java garbage collector
        for(int i = crumbs.size() - 1; i >= 0; i--)
        {
            crumbs.get(i).setNext(i, crumbs.get(i).next(i).next(i));
        }
    }

    public boolean contains(type data)
    {
        // variables
        int depth = 1;
        int listHeight = this.height();
        int nxt;
        Node<type> temp = head;

        //find
        while((listHeight - depth) >= 0)
        {
            nxt = listHeight - depth;
            //check for null
            if(temp.next(nxt) != null)
            {
                if(temp.next(nxt).value().compareTo(data) == 0)
                {
                    return true;
                }
                else if(temp.next(nxt).value().compareTo(data) > 0)
                {
                    depth++;
                }
                else // nxt < data
                {
                    temp = temp.next(nxt);
                }
            }
            else
            {
                depth++;
            }
        }
        return false;
    }

    public Node<type> get(type data)
    {
         // variables
         int depth = 1;
         int listHeight = this.height();
         int nxt;
         Node<type> temp = head;
 
         //find
         while((listHeight - depth) >= 0)
         {
             nxt = listHeight - depth;
             //check for null
             if(temp.next(nxt) != null)
             {
                 if(temp.next(nxt).value().compareTo(data) == 0)
                 {
                     return temp.next(nxt);
                 }
                 else if(temp.next(nxt).value().compareTo(data) > 0)
                 {
                     depth++;
                 }
                 else // nxt < data
                 {
                     temp = temp.next(nxt);
                 }
             }
             else
             {
                 depth++;
             }
         }
         return null;
    }

    public static double difficultyRating()
    {
        return 4.8;
    }
    public static double hoursSpent()
    {
        return 20.5;
    }
    
    //incresses or decresses list height 
    private void growSize()
    {
        int propertySize = (int)Math.ceil(Math.log(size)/Math.log(2));
        int currentHeight = this.height();
        Node<type> temp = head.next(currentHeight - 1);
        Node<type>lastGrown = head;

        if(currentHeight < propertySize)// grow
        {
            head.grow();
            while(temp != null)
            {
                if(temp.maybeGrow())
                {
                    lastGrown.setNext(currentHeight, temp);
                    lastGrown = lastGrown.next(currentHeight);
                }
                temp = temp.next(currentHeight - 1);
            }
        }
    }
    private void shrinkSize()
    {
        int propertySize = (int)Math.ceil(Math.log(size)/Math.log(2));
        int currentHeight = this.height();
        Node<type> temp = head.next(propertySize - 1);

        if(propertySize <= 0)
            propertySize = 1;
        if(currentHeight > propertySize)// small bouy
        {
            head.trim(propertySize);
            Node<type> nxt;
            while(temp != null)
            {
                nxt =temp.next(propertySize - 1);
                temp.trim(propertySize);
                temp = nxt;
            }
        }
    }
    //function to create a new height for new Node<type>
    private int randHeight(int max)
    {
        int newHeight;
        for(newHeight = 1; newHeight < max; newHeight++)
            if(Math.random() < 0.5)
                break;
        return newHeight;
    }

    public void printList()
    {
        Node<type> temp = head;
        int numStar;
        int height = this.height();
        int size = this.size() + 1;
        char [][] graphic = new char[size][height];

        System.out.println("\n\n\n\n");
        for(int r = 0; r < size; r++)
        {
            for(int c = 0; c < height; c++)
                graphic[r][c] = ' ';
        }
        for(int r = 0; r < size; r++)
        {
            numStar = temp.height();
            for(int c = 0; c < numStar; c++)
                graphic[r][c] = '*';
            if(temp.next(0) == null)
                break;
            temp = temp.next(0);
        }
        for(int c = height - 1; c >= 0; c--)
        {
            for(int r = 0; r < size; r++)
                System.out.print(graphic[r][c]);
            System.out.println();
        }
        temp = head;
        for(int e = 0; e < size; e++)
        {
            if(temp == null)
             System.out.print("N");
            else
                System.out.print(temp.value());
            if(temp.next(0) == null)
                break;
            temp =temp.next(0);
        }
        

        System.out.println("\n");
        temp = head;
        for(int e = 0; e < size; e++)
        {
            System.out.print(temp.value() + "(" + temp.height() + ")  ");
            if(temp.next(0) == null) 
            {
                break;
            }
            else temp = temp.next(0);
        }

        System.out.println("\n");
        for(int r = height - 1; r >=0; r--)
        {
            temp = head;
            System.out.print("lvl " + (r+1) + ": ");
            for(int c = size - 1; c >=0; c--)
            {
                System.out.print(temp.value() + " -> ");
                if(temp.next(r) == null) 
                {
                    System.out.print("null");
                    break;
                }
                else temp = temp.next(r);
            }
            System.out.println();
        }
    }

}
