<?php
//Turn off error reporting
error_reporting(E_ALL ^ E_NOTICE ^ E_WARNING);

//Create fields for the database
//Server, username, password, database

$dbhost = "localhost";
$dbuser = "seetoh88_mp18";
$dbpass = "etnetn87";
$dbdb = "seetoh88_mp18";

//Connect to mySQL
$connect = mysql_connect($dbhost, $dbuser, $dbpass)
or die("OMG STOP GIVING ME CONNECTION ERROR");

//Select the database
mysql_select_db($dbdb)or die("database selection error");

//Retrieve the login details via POST
$username = $_POST['username'];
$password = $_POST['password'];

//Query the table android login
$query = mysql_query("SELECT * FROM member WHERE username='$username' AND password='$password'");

//check if there any results returned
$num = mysql_num_rows($query);

//If a record was found matching the details entered in the query
if($num == 1){
	//Create a while loop that places the returned data into the array
	while($list=mysql_fetch_assoc($query)){
		//Store the returned data into a variable
		$output = $list;

		//Encode the returned data in JSON format
		echo json_encode($output);

	}
	//Close the connection
	mysql_close();
}



?>