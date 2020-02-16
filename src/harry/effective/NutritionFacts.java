package harry.effective;

/**
 * 
 * @author Harry
 *
 */
public class NutritionFacts {
	private int servingSize;
	private int servings;
	private int calories;
	
	private NutritionFacts(Builder builder){
		this.servingSize = builder.servingSize;
		this.servings = builder.servings;
		this.calories = builder.calories;
	}
	
	
	@Override
	public String toString() {
		return "NutritionFacts [servingSize=" + servingSize + ", servings=" + servings + ", calories=" + calories + "]";
	}

	public static class Builder{
		private int servingSize = 0;
		private int servings = 0;
		private int calories = 0;
		
		public Builder(int val){
			this.servingSize = val;
		}
		
		public Builder servings(int val){
			this.servings = val;
			
			return this;
		}
		
		public Builder calories(int val){
			this.calories = val;
			
			return this;
		}
		
		public NutritionFacts build(){
			return new NutritionFacts(this);
		}
	}
	
	public static void main(String[] args) {
		NutritionFacts build = new NutritionFacts.Builder(100).servings(200).calories(300).build();
		System.out.println(build);
	}
}
