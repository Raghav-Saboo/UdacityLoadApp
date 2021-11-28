# Project Title

Load App

## Getting Started

Load App is an app to download a file from the Internet by clicking on a custom-built button where:

- width of the button gets animated from left to right
- text gets changed based on different states of the button
- circle gets be animated from 0 to 360 degrees

A notification will be sent once the download is complete. When a user clicks on the notification, the user lands on detail activity and the notification gets dismissed. In detail activity, the status of the download will be displayed and animated via MotionLayout upon opening the activity.

### Installation

To get the project running on your local machine, you need to follow these steps:

**Step 1: Clone the repo**

Use this to clone it to your local machine:
```bash
git clone https://github.com/Raghav-Saboo/UdacityLoadApp.git
```

**Step 2: Run the project and check that it compiles correctly**

Open the project in Android Studio and click the Run ‘app’ button, check that it runs correctly and you can see the app in your device or emulator.

## Testing

Explain the steps needed to run any automated tests

### Break Down Tests

Explain what each test does and why

```
Examples here
```
## Project Instructions

- Create a radio list of the following options where one of them can be selected for downloading:
    - https://github.com/bumptech/glide
    - https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter
    - https://github.com/square/retrofit
- Create a custom loading button by extending View class and assigning custom attributes to it
- Animate properties of the custom button once it’s clicked
- Add the custom button to the main screen, set on click listener and call download() function with selected Url
- If there is no selected option, display a Toast to let the user know to select one.
- Once the download is complete, send a notification with custom style and design
- Add a button with action to the notification, that opens a detailed screen of a downloaded repository
- Create the details screen and display the name of the repository and status of the download
- Use declarative XML with motionLayout to coordinate animations across the views on the detail screen
- Add a button to the detail screen to return back to the main screen.


