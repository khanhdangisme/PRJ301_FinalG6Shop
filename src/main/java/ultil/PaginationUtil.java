/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ultil;

/**
 *
 * @author email
 */
public class PaginationUtil {
    public static final int NUMBER_OF_ITEMS_PAER_PAGE = 6;
    
    public static int getTotalPages(int countItem){
        return (int) Math.ceil((double) countItem / PaginationUtil.NUMBER_OF_ITEMS_PAER_PAGE);
    }
}
