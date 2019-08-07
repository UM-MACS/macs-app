
var crypto = require('crypto');
var uuid = require('uuid');
var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');

// connect to mysql
var con = mysql.createConnection({
	host:'localhost',
	user: 'root',
	password: '',
	database: 'masoccdata'
});

var app = express();
app.use(bodyParser.json({limit: '50mb',extended: true}));
app.use(bodyParser.urlencoded({limit: '50mb',extended: true}));

//password ultil
var genRandomString = function(length){
	return crypto.randomBytes(Math.ceil(length/2))
		.toString('hex') //convert to hex format
		.slice(0,length);
};

var sha512 = function(password,salt){
	var hash = crypto.createHmac('sha512',salt); 
	hash.update(password);
	var value = hash.digest('hex');
	return{
		salt:salt,
		passwordHash:value
	};
};

function saltHashPassword(userPassword){
	var salt = genRandomString(16);
	var passwordData = sha512(userPassword,salt);
	return passwordData;
}

function checkHashPassword(userPassword,salt){
	var passwordData = sha512(userPassword,salt);
	return passwordData;
}


/* Register Activity */
//register as patient
app.post('/register/',(req,res,next)=>{

	var post_data = req.body; //get POST params
	var name = post_data.name;
	var email = post_data.email;
	var contactNo = post_data.contact;
	var age = post_data.age;
	// var photo = post_data.photo;

	// var name = 'l';
	// var email = 'ju@gmail.com';
	// var contactNo = '01923933';
	// var age = '12';
	// var photo = post_data.photo;
	// var password = 'abc';


	var plain_password = post_data.password;
	var hash_data = saltHashPassword(plain_password);
	var password = hash_data.passwordHash;
	var salt = hash_data.salt;
	console.log('email: ',email);
	console.log('name: ',name);
	console.log('password: ',password);
	console.log('contact: ',contactNo);
	console.log('age: ',age);
	console.log('salt: ',salt);
	

	con.query('SELECT * FROM usertable WHERE email=?',[email],
		function(err,result,fields){
			con.on('error',function(err){
				console.log('[MYSQL error]',err);
			});
		console.log('result ',result);
		if(result && result.length)	{
			console.log('User already exists');
			res.json('User already exists');
		}
		else{
			console.log('User new user');
			con.query('INSERT INTO usertable (name, email, password, contactNo, age, salt) VALUES (?,?,?,?,?,?)',[name,email,password,contactNo,age,salt],
			 function(err,result,fields){
				if(err){
					console.log('success: 0');
					res.json([{success:'0'}]);
					// throw err;	
				}
				else{
					console.log('success');
					res.json([{success:'1'}]);	
				} 
				
			});
				
			
		}
	});
})
//register as caregiver
app.post('/register2/',(req,res,next)=>{

	var post_data = req.body; //get POST params
	var name = post_data.name;
	var email = post_data.email;
	var contactNo = post_data.contact;
	var age = post_data.age;
	var relationship = post_data.relationship;

	// var photo = post_data.photo;

	// var name = 'l';
	// var email = 'ju@gmail.com';
	// var contactNo = '01923933';
	// var age = '12';
	// var photo = post_data.photo;
	// var password = 'abc';


	var plain_password = post_data.password;
	var hash_data = saltHashPassword(plain_password);
	var password = hash_data.passwordHash;
	var salt = hash_data.salt;
	console.log('email: ',email);
	console.log('name: ',name);
	console.log('password: ',password);
	console.log('contact: ',contactNo);
	console.log('age: ',age);
	console.log('salt: ',salt);
	

	con.query('SELECT * FROM caregivertable WHERE email=?',[email],
		function(err,result,fields){
			con.on('error',function(err){
				console.log('[MYSQL error]',err);
			});
		console.log('result ',result);
		if(result && result.length)	{
			console.log('User already exists');
			res.json('User already exists');
		}
		else{
			console.log('User new user');
			con.query('INSERT INTO caregivertable (name, email, password, contactNo, age, salt, relationship) VALUES (?,?,?,?,?,?,?)'
				,[name,email,password,contactNo,age,salt,relationship],
			 function(err,result,fields){
				if(err){
					console.log('success: 0');
					res.json([{success:'0'}]);
					// throw err;	
				}
				else{
					console.log('success');
					res.json([{success:'1'}]);	
				} 
				
			});
				
			
		}
	});
})




/* Login Activity */
//login as patient
app.post('/login/',(req,res,next)=>{
	var post_data = req.body;

	var user_password = post_data.password;
	var email = post_data.email;

	// var user_password = 'hello';
	// var email = 'he@gmail.com';

	con.query('SELECT * FROM usertable WHERE email=? ',[email],
		function(error,result,fields){
			con.on('error',function(err){
				console.log('mysql error',err);
				res.json('error',err);
			});
		if(result && result.length){
			var salt = result[0].salt; //get salt 
			console.log('salt',salt);
			var encrypted_password = result[0].password;
			console.log('password: ',encrypted_password);
			var hashed_password = checkHashPassword(user_password,salt).passwordHash;

			if(encrypted_password == hashed_password){
				res.json([{success:'1',name: result[0].name, email: result[0].email}]); //return all 
			}
			else{
				//wrong password
				res.json([{success:'0'}]);
			}
		}
		else{
			//wrong email
			res.json([{success: '-1'}]);
		}
		});
})
//login as caregiver
app.post('/login2/',(req,res,next)=>{
	var post_data = req.body;

	var user_password = post_data.password;
	var email = post_data.email;

	// var user_password = 'hello';
	// var email = 'he@gmail.com';

	con.query('SELECT * FROM caregivertable WHERE email=? ',[email],
		function(error,result,fields){
			con.on('error',function(err){
				console.log('mysql error',err);
				res.json('error',err);
			});
		if(result && result.length){
			var salt = result[0].salt; //get salt 
			console.log('salt',salt);
			var encrypted_password = result[0].password;
			console.log('password: ',encrypted_password);
			var hashed_password = checkHashPassword(user_password,salt).passwordHash;

			if(encrypted_password == hashed_password){
				res.json([{success:'1',name: result[0].name, email: result[0].email}]); //return all 
			}
			else{
				//wrong password
				res.json([{success:'0'}]);
			}
		}
		else{
			//wrong email
			res.json([{success: '-1'}]);
		}
		});
})




/* Change Password Acticity*/
app.post('/changepassword/',(req,res,next)=>{
	var post_data = req.body;

	var email = post_data.email;
	var type = post_data.type;
	var password = post_data.password;
	var plain_password = post_data.newPass;

	var hash_data = saltHashPassword(plain_password);
	var newPass = hash_data.passwordHash;
	var newSalt = hash_data.salt;
	
	if(type == 'Patient'){
		con.query('SELECT * FROM usertable WHERE email=? ',[email],
		function(error,result,fields){
			con.on('error',function(err){
				console.log('mysql error',err);
				res.json('error',err);
			});
		if(result && result.length){
			var salt = result[0].salt; //get salt 
			console.log('salt',salt);
			var encrypted_password = result[0].password;
			console.log('correct password: ',encrypted_password);
			var hashed_password = checkHashPassword(password,salt).passwordHash;
			console.log('user password: ',hashed_password);

			if(encrypted_password == hashed_password){
				//if success 
				con.query('UPDATE usertable SET password=?, salt=? WHERE email =?',
				 [newPass,newSalt,email], function(error,result,fields){
				 	if(error){
				 		//fail to update
				 		res.json([{success: '0'}]);
				 	} else{
				 		//update success
				 		res.json([{success:'1'}]);
				 	}
				 });
			}
			else{
				//wrong password
				res.json([{success:'-1'}]);
			}
		}
		});
	} else{ //if type is caregiver
		con.query('SELECT * FROM caregivertable WHERE email=? ',[email],
		function(error,result,fields){
			con.on('error',function(err){
				console.log('mysql error',err);
				res.json('error',err);
			});
		if(result && result.length){
			var salt = result[0].salt; //get salt 
			console.log('salt',salt);
			var encrypted_password = result[0].password;
			console.log('password: ',encrypted_password);
			var hashed_password = checkHashPassword(password,salt).passwordHash;

			if(encrypted_password == hashed_password){
				//if success 
				con.query('UPDATE caregivertable SET password=?, salt=? WHERE email =?',
				 [newPass,newSalt,email], function(error,result,fields){
				 	if(error){
				 		//fail to update
				 		res.json([{success: '0'}]);
				 	} else{
				 		//update success
				 		res.json([{success:'1'}]);
				 	}
				 });
			}
			else{
				//wrong password
				res.json([{success:'-1'}]);
			}
		}
		});
	}
})




/* Emotion Activity*/
app.post('/emotion/',(req,res,next)=>{
	var post_data = req.body; //get POST params

	var email = post_data.email;
	var type = post_data.type;
	var date = post_data.date;
	var expression = post_data.expression;
	

	con.query('INSERT INTO userEmotionData (email, type, date, expression) VALUES (?,?,?,?)'
		,[email,type,date,expression],
		function(err,result,fields){
				if(err){
					console.log('success: 0');
					res.json([{success:'0'}]);
					// throw err;	
				}
				else{
					console.log('success');
					res.json([{success:'1'}]);	
				} 	
	});
})




/* Event Assessment Activity*/
app.post('/eAssessment/',(req,res,next)=>{
	var post_data = req.body; //get POST params

	var email = post_data.email;
	var type = post_data.type;
	var q1 = post_data.q1;
	var q2 = post_data.q2;
	var q3 = post_data.q3;
	var q4 = post_data.q4;
	var q5 = post_data.q5;
	var q6 = post_data.q6;
	var q7 = post_data.q7;
	var q8 = post_data.q8;
	

	con.query('INSERT INTO eventAssessmentTable (email, type, q1, q2, q3, q4, q5, q6, q7, q8) VALUES (?,?,?,?,?,?,?,?,?,?)'
		,[email,type,q1,q2,q3,q4,q5,q6,q7,q8],
		function(err,result,fields){
				if(err){
					console.log('success: 0');
					res.json([{success:'0'}]);
					// throw err;	
				}
				else{
					console.log('success');
					res.json([{success:'1'}]);	
				} 	
	});
})




/* Appointment Scheduling Activity */
//get Appointment 
app.post('/getAppointment/',(req,res,next)=>{
	var post_data = req.body; //get POST params

	var email = post_data.email;
	// var email ='l@gmail.com';
	var jsonArray =[];
	

	con.query('SELECT * FROM patientAppointment WHERE email=?'
		,[email],
		function(err,result,fields){
			con.on('error',function(err){
			console.log('mysql error',err);
			res.json([{success:'0'}]);
		});
		if(result && result.length)	{
			for (var i = 0; i < result.length; i++) {
				jsonArray.push({
					success: '1', 
					remark: result[i].remark, 
					appointmentDate: result[i].appointmentDate,
					appointmentTime: result[i].appointmentTime,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//set Appointment
app.post('/setAppointment/',(req,res,next)=>{
	var post_data = req.body;

	var email = post_data.email;
	var type = post_data.type;
	var remark = post_data.remark;
	var date = post_data.date;
	var time = post_data.time;

	con.query('INSERT INTO patientAppointment (email, type, remark, appointmentDate, appointmentTime) VALUES (?,?,?,?,?)',
		[email,type,remark,date,time],
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		});
})

//delete Appointment
app.post('/deleteAppointment/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;

	con.query('DELETE FROM patientAppointment WHERE id=?',
		[id], function(err, result, fields){
			if(err){
				res.json([{success: '0'}]);
			}
			else{
				res.json([{success: '1'}]);
			}
		});
})

//update Appointment
app.post('/updateAppointment/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;
	var remark = post_data.remark;
	var date = post_data.date;
	var time = post_data.time;

	con.query('UPDATE patientAppointment SET remark=?, appointmentDate=?, appointmentTime=? WHERE id=?',
		[remark,date,time,id], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			}
			else{
				res.json([{success:'1'}]);
			}
		});
})




/*Forum Activity*/
//get forum posts
app.post('/getForumPost/',(req,res,next)=>{
	var jsonArray=[];
	con.query('SELECT * FROM forumdata ORDER BY id DESC LIMIT 30',
		function(err,result,fields){
			con.on('error',function(err){
			console.log('mysql error',err);
			res.json([{success:'0'}]);
		});
		if(result && result.length)	{
			for (var i = 0; i < result.length; i++) {
				jsonArray.push({
					success: '1', 
					name: result[i].name, 
					title: result[i].title,
					content: result[i].content,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//posting to forum
app.post('/postingToForum/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var name = post_data.name;
	var title = post_data.title;
	var content = post_data.content;

	con.query('INSERT INTO forumdata (email,name,title,content) VALUES (?,?,?,?)',
		[email,name,title,content], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})

//get my posts
app.post('/getMyPost/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var jsonArray=[];
	con.query('SELECT * FROM forumdata WHERE email=?',[email],
		function(err,result,fields){
			con.on('error',function(err){
			console.log('mysql error',err);
			res.json([{success:'0'}]);
		});
		if(result && result.length)	{
			for (var i = 0; i < result.length; i++) {
				jsonArray.push({
					success: '1', 
					name: result[i].name, 
					title: result[i].title,
					content: result[i].content,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//update posts
app.post('/updatePost/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;
	var title = post_data.title;
	var content = post_data.content;

	con.query('UPDATE forumdata SET title=?, content=? WHERE id=?',
		[title,content,id], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			}
			else{
				res.json([{success:'1'}]);
			}
		});
})

//delete post
app.post('/deletePost/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;

	con.query('DELETE FROM forumdata WHERE id=?',
		[id], function(err, result, fields){
			if(err){
				res.json([{success: '0'}]);
			}
			else{
				res.json([{success: '1'}]);
			}
		});
})






//testing
// app.get("/",(req,res,next)=>{
// 	console.log('password: 123456');
// 	var encrypt = saltHashPassword("123456");
// 	console.log('Encrypt pw hash: '+encrypt.passwordHash);
// 	console.log('Salt: '+encrypt.salt);

// })

// app.get("/",(req,res,next)=>{
// 	console.log('enter');

// })

//start server
app.listen(3000, ()=>{
	console.log('Restful running on port 3000');
});


