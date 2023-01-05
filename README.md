# A Course management System

## Helps you manage the workload for your courses

# Application functionality:
You add *Courses* to the application and then you can add the workload for the courses such as the assignments or
homework. For each workload, it will ask you to add date, time and the estimated time taken for you to finish the task. The
application will then create a schedule for you based on the dates and the homeworks that you have to do. You can put add
the marks you received and the weighing percentage of those marks and the program will return the current progress report
[your current grades]. You cna also change the status of the homework. This will help you keep up with your marks for the
courses and your current progress, of these combined should decrease your procrastination. All of the scheduling and progress
can also be seen visually with a proper schedule for the days and the homeworks for that day.

#Users for the application:
It is made for people who need help with procrastination and who need help with organising their tasks.
This will be especially useful for children studying in *schools* or *universities* but can be used by *anyone who does not
have enough time on their hands to meticulously make a schedule and organise* for all the work piling up for them.

# Personal interest?
I have always been interested in preparing an optimum schedule for myself which helps me relax. While taking
summer courses right after a short vacation, I realised how easy it is for pressure to make you less organised which makes
you less productive/procrastinate and the cycle begins again. In such cases, I think having an management system where you
can schedule your courses' work and keep up with progress you are making in the course can ease the pressure of getting
the tasks done. Also, I think that having a user friendly interface that automatically schedules tasks for you can be
really helpful for almost everyone.

# User story for my application:
- As a user, I want to add add my courses [unique] to the system
- As a user, I want to keep the homeworks of specific courses in the system
- As a user, I want the homework added to have a deadline, duration for it, the grade and the weighing %
- As a user, I want to schedule my homeworks based on their deadlines
- As a user, I want the system to prevent me from adding homeworks for more than 20 hours in one day
- As a user, I want to visualise the schedule made
- As a user, I want to keep track of the grades of the received and how I am doing in the course
- As a user, I want to keep track of the homework's status done or not
- As a user, I want to save the the information put in my application menu, when trying to quit, it will ask you
    whether you want to save your data or not.
- As a user, I want to get the option to reload the data from the course management app.


# How to improve the project
- Increase cohesion by creating Schedule as its own class which would handle schedule making process and could also be saved as JSon along with homeworks, courses and students
- Implement observer pattern such that changes made can automatically be logged into EventLog class. This includes having a Java Observable class which would be inherited by Student and Courses.
There would be also an observer interface which would be implemented by the EventLog class.
- Implement composite pattern as my current data structure forms a tree diagram with student -> courses -> homework. I would implement a 
  composite pattern to parse through the students for tasks such as displaying the schedule or making the schedule. Course and Homework would extend
  a workload abstract class, then Courses would be the composite, workload class would be the component, and the homeworks would be the leaf. This would
  Also allow me to add nested courses which could be acting as sessions within the course 
  [i know that counts as extra feature but i would use the composite pattern for parsing through my current tree structure too]
