/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

/**
 *
 * @author drawven
 */
public class SingleDay {
    
    int hours;
    int start;
    int finish;
    boolean off;

    public SingleDay(int hours, int start, int finish, boolean off) {
        this.hours = hours;
        this.start = start;
        this.finish = finish;
        this.off = off;
    }
    
    
    
}
