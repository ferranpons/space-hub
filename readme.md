Clean Android Architecture using MVP + RxJava + Retrolambda + Retrofit
======================================================================




## Getting Started

### Prerequisites

minSdkVersion >= 

### Download



### Permissions

You will need to add this line to your Manifest. Obviously this library needs internet. xD

	<uses-permission android:name="android.permission.INTERNET" />
	
	
### Usage

#### Considerations

This library was developed using the ***MVP*** (Model-View-Presenter) pattern. This means that every method of the presenter
called doesn't returns anything directly all retrieved data is returned using one of the view methods
declared in the ViewInterface that you will have to add in your activities or fragments.


