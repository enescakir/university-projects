## Simple Airline Reservation Web App

It's an in-house web-based airlines reservation application. Administrators can generate general instances and employees make reservations for customers. They can select seat on plane easily.

I developed my own little PHP framework. It is heavily inspired from Laravel Framework.

### Running App
- Put your database credentials to `config.php`
- Serve application with `php -S localhost:8888`.
- Visit address `localhost:8888` from your internet browser.
- To create database scheme visit `localhost:8888/database`
- First administrator activated automatically

<img src="/cmpe321/project3/report/cmpe321_p3_ss1.png" width="70%">

<img src="/cmpe321/project3/report/cmpe321_p3_ss2.png" width="70%">

<img src="/cmpe321/project3/report/cmpe321_p3_ss4.png" width="70%">

<img src="/cmpe321/project3/report/cmpe321_p3_ss6.png" width="70%">

<img src="/cmpe321/project3/report/cmpe321_p3_ss7.png" width="70%">

### Structure
Entry point of my application is index.php. After that point, my `Router` handles routing.

I have 3 important directory.
1. `App` directory contains project specific files, such as `Models`, `Controllers`, `Routes`, `Views`.
2. `Core` directory contains my framework classes, such as `QueryBuilder`, `Auth`, `Router`, `Request`.
3. `Public` directory contains `stylesheets`, `scripts` etc.

### Further Developments
You can use this framework in your CmpE321 project easily. If you're not sure what you're doing, don't touch the `Core` folder.

Your main workspace is `App` folder. Add your project's routes, models, controllers and views to corresponding folders.

**Note:** Don't forget to update your autoload file with `composer dumpautoload` after you added new files.
