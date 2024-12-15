# Eat-o-Meter: Calorie Tracking System 🍏🥗

## I. Project Overview 📝
Eat-o-Meter is a Java-based console application designed to help users track their daily calorie intake and exercise, manage their health goals, and maintain a balanced lifestyle. It provides a user-friendly interface for logging food, exercises, and tracking progress. By calculating metrics such as Basal Metabolic Rate (BMR) and Total Daily Energy Expenditure (TDEE), it helps users make informed decisions to achieve their fitness goals.

## Key Features 🏮
- 🔐 **Sign Up/Log In**: Users will sign up or log in to access the manu and other features.
- 🔥 **Calculate and Display Daily Calorie**: Calculates your daily calorie intake based on your data and goals then display it on the home menu.
- 📔 **Log Items to Diary**: Log food or exercise in the diary with the details needed by the system.
- 🔎 **View Log History**: Displays all your logged items with date and calorie details.
- 📊 **View Weekly Report**: Display weekly calorie details and logged items.
- ⚖ **Weight Progress**: Weekly weight update to show your progress toward your goal.
- 🗑️ **Delete Account**: Allows to delete your account and the connected data.

## II. Explanation of OOP Principles Applied 💡
This project utilizes Object-Oriented Programming (OOP) principles to ensure modularity, reusability, and maintainability:
- 🔒 **Encapsulation**: User-related data (e.g., name, age, weight, TDEE) is encapsulated in the `UserProfile` class while the item attributes (food, exercise, calories consumed/burned) is mainly encapsulated in `DiaryEntry`. The private attributes are accessed and modified through public methods.
   
- 📚 **Inheritance**: The `DiaryEntry` class serves as a base for `FoodEntry` and `ExerciseEntry`, containing shared attributes (e.g., username, type, category, itemName, calories, and date). `FoodEntry` adds properties like `quantity` and `unit`, while ExerciseEntry includes `duration` and `unit`. This structure promotes code reusability and organization, making the Eat-o-Meter system scalable, maintainable, and flexible.
  
- 🔄 **Polymorphism**: This principle is used in the `dailyCalorieGoal()` method to calculate different calorie goals based on the user's objective (e.g., weight loss or gain). The same method name is used, but its behavior changes dynamically by invoking different internal methods (`calculateCalorieDeficit()` for weight loss and `calculateCalorieSurplus()` for weight gain). This allows the program to handle various user goals with a single method, improving code flexibility, reusability, and scalability.
  
- 🧩 **Abstraction**: The `DiaryManager` class abstracts the complexity of managing food logs, exercises, and summaries, making it easier for users to interact with the system without worrying about the underlying logic. 

## 🌱 Details of the Chosen SDG and Its Integration into the Project 
This project aligns with **SDG 3: Good Health and Well-being**, focusing on promoting healthy lifestyles and well-being. By tracking calories, exercises, and weight progress, the system enables users to:
- 🎯 Set personalized health goals. 
- 🍽️🏋️ Track daily food intake and physical activities. 
- 💪 Adjust lifestyle habits to maintain or improve health. 

The project encourages users to adopt healthy habits and make informed decisions about their daily nutrition and exercise, contributing to their long-term well-being. 🌍❤️


## IV. Instructions for Running the Program 🚀
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
- Setup your goals in using the system 🎯
- Access the menu options to log food or exercise, track calories, and view weekly reports. 📊
