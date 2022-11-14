package src.cineplex;


/** 
  Filter choices for the admin to set on moviegoers, 
  allowing them to view Top 5 Listings either by 
  viewer rating or by sales
 */
public enum Choice {
    /**
     * this chooses to print top 5 rating
     */
    RATINGTOP,
    /** 
     * this chooses to print top 5 sales
     */
    SALESTOP,
    /**
     *  this chooses to print both.
     */
    BOTH
}