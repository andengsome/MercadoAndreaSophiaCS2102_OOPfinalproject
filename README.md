# Eat-o-Meter: Calorie Tracking System 🍏🥗

## 📝 I. Project Overview 
Eat-o-Meter is a Java-based console application designed to help users track their daily calorie intake and exercise, manage their health goals, and maintain a balanced lifestyle. It provides a user-friendly interface for logging food, exercises, and tracking progress. By calculating metrics such as Basal Metabolic Rate (BMR) and Total Daily Energy Expenditure (TDEE), it helps users make informed decisions to achieve their fitness goals.

## 💡 II. Explanation of OOP Principles Applied 
This project utilizes Object-Oriented Programming (OOP) principles to ensure modularity, reusability, and maintainability:
- 🔒 **Encapsulation**: User-related data (e.g., name, age, weight, TDEE) is encapsulated in the `UserProfile` class while the item attributes (food, exercise, calories consumed/burned) is mainly encapsulated in `DiaryEntry`. The private attributes are accessed and modified through public methods.
   
- 📚 **Inheritance**: This principle exhibits in the parent class `DiaryEntry` and the subclasses `FoodEntry` and `ExerciseEntry`. The parent class containes common attributres (type, category, item_name, calories, date, etc.) that serves as a generel template for all diary entries, whether they relate to food or exercise. `FoodEntry` adds specific properties related to food, such as quantity and unit (grams, slices, pieces, etc.) while `ExerciseEntry` adds properties like duration and the unit 'minutes'. These classes ensure code reusability and organization and building a more scalable, maintainable, and organized Eat-o-Meter system.
  
- 🔄 **Polymorphism**: Methods like `calculateCalories()` exhibit polymorphism, allowing different types of calorie calculations based on user goals (e.g., weight loss or gain).
  
- 🧩 **Abstraction**: The `DiaryManager` class abstracts the complexity of managing food logs, exercises, and summaries, making it easier for users to interact with the system without worrying about the underlying logic. 

## 🌱 III. Details of the Chosen SDG and Its Integration into the Project 
This project aligns with **SDG 3: Good Health and Well-being**, focusing on promoting healthy lifestyles and well-being. By tracking calories, exercises, and weight progress, the system enables users to:
- 🎯 Set personalized health goals. 
- 🍽️🏋️ Track daily food intake and physical activities. 
- 💪 Adjust lifestyle habits to maintain or improve health. 

The project encourages users to adopt healthy habits and make informed decisions about their daily nutrition and exercise, contributing to their long-term well-being. 🌍❤️

## 🚀 IV. Instructions for Running the Program 

### ⚙️ Prerequisites :
- JDK 11 or later installed on your system. ☕
- MySQL 8.0.40 or later installed and running. 🛠️

### Steps to Run the Program:
1. **Clone or Download the Repository:**
   - Clone the repository:
     ```
     gh repo clone andengsome/MercadoAndreaSophiaCS2102_OOPfinalproject\
     ```

   - You can also download the repository as ZIP file and extract it on your device.

2. **Set up the MySQL Database:**
- Create a database named `eatometer`. 🗃️
- Import the provided SQL scripts (`init.sql`) to create the necessary tables (`users`, `diary_entries`, `log_items`, `weight_progress`). 📑

3. **Compile and Run the Program:**
- Navigate to the project directory and compile the Java files:
  ```
  javac src/main/*.java
  ```
- Run the program:
  ```
  java src.main.MainMenu
  ```

4. **Use the Application:**
- Sign up or log in as a user. 🔑
- Setup goals 🎯
- Access the menu options to log your food or exercise, track calories, and view weekly reports. 📊
