
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

console.log()
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
	// var plain_password = 'abc';


	var plain_password = post_data.password;
	var hash_data = saltHashPassword(plain_password);
	// var hash_data = saltHashPassword(plain_password);
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
//register as specialist
app.post('/register3/',(req,res,next)=>{

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
	

	con.query('SELECT * FROM specialistTable WHERE email=?',[email],
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
			con.query('INSERT INTO specialistTable (name, email, password, contactNo, age, salt) VALUES (?,?,?,?,?,?)'
				,[name,email,password,contactNo,age,salt],
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

			else if(user_password == salt){
				res.json([{success:'2'}]);
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
//login as specialist
app.post('/login3/',(req,res,next)=>{
	var post_data = req.body;

	var user_password = post_data.password;
	var email = post_data.email;

	// var user_password = 'hello';
	// var email = 'he@gmail.com';

	con.query('SELECT * FROM specialistTable WHERE email=? ',[email],
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

/* Reset Password Activity*/
app.post('/sendSaltToEmail/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	// var email = 'o@gmail.com';
	// var tempPass = "shkjsdfhuifhddk"
	var tempPass;


	con.query('SELECT * FROM usertable WHERE email=? ',[email],
		function(error,result,fields){
			con.on('error',function(err){
				console.log('mysql error',err);
				res.json('error',err);
			});
		if(result && result.length){
			tempPass = result[0].salt;
			sendEmail(tempPass,email);
			res.json([{success: '1'}]);

		}
		else{
			//wrong email
			res.json([{success: '-1'}]);
			console.log("Email not Found");

		}
		});

	function sendEmail(tempPass, email){
		console.log('temporary password is '+ tempPass);
		var nodemailer = require('nodemailer');
		var transporter = nodemailer.createTransport({
		service: 'gmail',
		auth:{
			user: 'masocc.noreply@gmail.com',
			pass: 'masoccfyp'
		}
	});

	var mailOptions = {
		from: 'masocc.noreply@gmail.com',
		to: email,
		subject: 'Reset Your Password of MASOCC Account',
		html: '<br><img src="https://fontmeme.com/permalink/191129/1d5a554c0bf94354d7c10c92bae194b7.png"><br><font color="#383838">'+
		'<h1>Greetings,</h1><h2>Please log in using your temporary password: <i><b> '
		+tempPass+'</i></b><br/> to reset your password.</h2><br><h3>If you did not perform this action,'+
		' please ignore this email.</h3><br><h4>With Kind Regards,<br/><br/><i>MASOCC Team</i></h4></font>'
	};

	transporter.sendMail(mailOptions,function(error,info){
		if(error){
			console.log(error);
		} else{
			console.log('Email sent: '+info.response);
		}
	});	
}	
})

app.post('/resetPassword/',(req,res,next)=>{
	var post_data = req.body;

	var email = post_data.email;
	var type = post_data.type;
	var password = post_data.password;

	var hash_data = saltHashPassword(password);
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
		});
	}
})





/* Emotion Activity*/
//submit emotion
app.post('/emotion/',(req,res,next)=>{
	var post_data = req.body; //get POST params

	var email = post_data.email;
	var type = post_data.type;
	var date = post_data.date;
	var expression = post_data.expression;
	var output = post_data.output;
	

	con.query('INSERT INTO userEmotionData (email, type, date, expression,analysis) VALUES (?,?,?,?,?)'
		,[email,type,date,expression,output],
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

//analyse emotion
app.post('/getEmotion/',(req,res,next)=>{
	var post_data = req.body;
	// var email = post_data.email;
	var jsonArray=[];

	con.query('SELECT * FROM userEmotionData', 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				if(result && result.length)	{
					for (var i = 0; i < result.length; i++) {
						jsonArray.push({
							success: '1', 
							expression: result[i].expression,
							id: result[i].id});
					}
					res.json(jsonArray);
				} else{
					res.json([{success:'-1'}]);
				}
			}
		})
})
//update emotion
app.post('/updateEmotion/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;
	var analysis = post_data.analysis;
	// var id = '1';
	// var analysis = 'Positive';

	con.query('UPDATE userEmotionData SET analysis=? WHERE id=?',
		[analysis,id], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			}
			else{
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
	var parentID = '';
	con.query('SELECT * FROM forumdata WHERE parentID=? ORDER BY id DESC LIMIT 30',
		[parentID],
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
					anonymous: result[i].anonymous,
					pinned: result[i].pinned,
					date: result[i].date,
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
	var anonymous = post_data.anonymous;
	var date = post_data.date;

	con.query('INSERT INTO forumdata (email,name,title,content,anonymous,date) VALUES (?,?,?,?,?,?)',
		[email,name,title,content,anonymous,date], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})


//reply to post
app.post('/postReply/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var name = post_data.name;
	var content = post_data.content;
	var parentID = post_data.parentID;
	var date = post_data.date;

	con.query('INSERT INTO forumdata (email,name,content,parentID,date) VALUES (?,?,?,?,?)',
		[email,name,content,parentID,date], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})

//get reply post
app.post('/getReplyPost/',(req,res,next)=>{
	var post_data = req.body;
	var parentID = post_data.parentID;
	var jsonArray=[];
	con.query('SELECT * FROM forumdata WHERE parentID=?',
		[parentID],
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
					content: result[i].content,
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})


//get my posts
app.post('/getMyPost/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var jsonArray=[];
	con.query('SELECT * FROM forumdata WHERE email=? ORDER BY id DESC',[email],
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
					date: result[i].date,
					parentID: result[i].parentID,
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

//pin post
app.post('/pinPost/',(req,res,next)=>{
	var post_data = req.body;
	var id = post_data.id;
	var pin = post_data.pin;

	con.query('UPDATE forumdata SET pinned=? WHERE id=?',
		[pin,id], function(error, result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		});
})

//search post
app.post('/searchPost/',(req,res,next)=>{
	var post_data = req.body;
	var search = post_data.search;
	var jsonArray = []; 
	// var search = 'good';

	con.query('SELECT * FROM forumdata WHERE content LIKE ? OR title LIKE ?',
		['%'+search+'%', '%'+search+'%'], function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				if(result && result.length)	{
					for (var i = 0; i < result.length; i++) {
						jsonArray.push({
						success: '1', 
						name: result[i].name, 
						title: result[i].title,
						content: result[i].content,
						anonymous: result[i].anonymous,
						pinned: result[i].pinned,
						parentID: result[i].parentID,
						date: result[i].date,
						id: result[i].id});
					}
					res.json(jsonArray);
				} else{
					res.json([{success:'-1'}]);
				}
			}
		})
})

//add to favourite list
app.post('/addToFavourite/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var postID = post_data.postID;

	con.query('INSERT INTO favouriteList (email,postID) VALUES (?,?)',
		[email,postID], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})

//remove from favourite list
app.post('/removeFavourite/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var postID = post_data.postID;

	con.query('DELETE FROM favouriteList WHERE email=? AND postID=?',
		[email,postID], function(err, result, fields){
			if(err){
				res.json([{success: '0'}]);
			}
			else{
				res.json([{success: '1'}]);
			}
		});
})

//get is favourite 
app.post('/getIsFavourite/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var postID = post_data.postID; 

	con.query('SELECT * FROM favouriteList WHERE email=? AND postID=?',
		[email,postID], function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				if(result && result.length)	{
					res.json([{success: '1'}]);
				} else{
					res.json([{success:'-1'}]);
				}
			}
		})
})

//view favourite list
app.post('/myFavouriteList/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var jsonArray=[];

	con.query('SELECT * FROM forumdata WHERE id in (SELECT postID FROM favouriteList WHERE email=?) ORDER BY id DESC', [email],
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
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//report post
app.post('/reportPost/',(req,res,next)=>{
	var post_data = req.body;
	var id = post_data.id;
	var report = post_data.report;

	con.query('UPDATE forumdata SET reported=? WHERE id=?',
		[report,id], function(error, result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		});
})

//get report post
app.post('/getReportedPost/',(req,res,next)=>{
	var post_data = req.body;
	var jsonArray=[];
	var reported = "true";

	con.query('SELECT * FROM forumdata WHERE reported=? ORDER BY id DESC',[reported],
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
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//get posts by id
app.post('/getPost/',(req,res,next)=>{
	var post_data = req.body;
	var id = post_data.id;
	var jsonArray=[];
	con.query('SELECT * FROM forumdata WHERE id=? ORDER BY id DESC',[id],
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
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})






/*Caregiver Forum Activity*/
//get forum posts
app.post('/getCaregiverForumPost/',(req,res,next)=>{
	var jsonArray=[];
	var parentID = '';
	con.query('SELECT * FROM caregiverforumdata WHERE parentID=? ORDER BY id DESC LIMIT 30',
		[parentID],
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
					anonymous: result[i].anonymous,
					pinned: result[i].pinned,
					date: result[i].date,
					id: result[i].id});

			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//posting to forum
app.post('/postingToCaregiverForum/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var name = post_data.name;
	var title = post_data.title;
	var content = post_data.content;
	var anonymous = post_data.anonymous;
	var date = post_data.date;

	con.query('INSERT INTO caregiverforumdata (email,name,title,content,anonymous,date) VALUES (?,?,?,?,?,?)',
		[email,name,title,content,anonymous,date], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})


//reply to post
app.post('/postReplyCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var name = post_data.name;
	var content = post_data.content;
	var parentID = post_data.parentID;
	var date = post_data.date;

	con.query('INSERT INTO caregiverforumdata (email,name,content,parentID,date) VALUES (?,?,?,?,?)',
		[email,name,content,parentID,date], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})

//get reply post
app.post('/getReplyPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var parentID = post_data.parentID;
	var jsonArray=[];
	con.query('SELECT * FROM caregiverforumdata WHERE parentID=?',
		[parentID],
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
					content: result[i].content,
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})


//get my posts
app.post('/getMyPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var jsonArray=[];
	con.query('SELECT * FROM caregiverforumdata WHERE email=? ORDER BY id DESC',[email],
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
					date: result[i].date,
					parentID: result[i].parentID,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//update posts
app.post('/updatePostCaregiver/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;
	var title = post_data.title;
	var content = post_data.content;

	con.query('UPDATE caregiverforumdata SET title=?, content=? WHERE id=?',
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
app.post('/deletePostCaregiver/',(req,res,next)=>{
	var post_data = req.body;

	var id = post_data.id;

	con.query('DELETE FROM caregiverforumdata WHERE id=?',
		[id], function(err, result, fields){
			if(err){
				res.json([{success: '0'}]);
			}
			else{
				res.json([{success: '1'}]);
			}
		});
})

//pin post
app.post('/pinPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var id = post_data.id;
	var pin = post_data.pin;

	con.query('UPDATE caregiverforumdata SET pinned=? WHERE id=?',
		[pin,id], function(error, result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		});
})

//search post
app.post('/searchPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var search = post_data.search;
	var jsonArray = []; 
	// var search = 'good';

	con.query('SELECT * FROM caregiverforumdata WHERE content LIKE ? OR title LIKE ?',
		['%'+search+'%', '%'+search+'%'], function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				if(result && result.length)	{
					for (var i = 0; i < result.length; i++) {
						jsonArray.push({
						success: '1', 
						name: result[i].name, 
						title: result[i].title,
						content: result[i].content,
						anonymous: result[i].anonymous,
						pinned: result[i].pinned,
						parentID: result[i].parentID,
						date: result[i].date,
						id: result[i].id});
					}
					res.json(jsonArray);
				} else{
					res.json([{success:'-1'}]);
				}
			}
		})
})

//report post
app.post('/reportPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var id = post_data.id;
	var report = post_data.report;

	con.query('UPDATE caregiverforumdata SET reported=? WHERE id=?',
		[report,id], function(error, result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		});
})

//get report post
app.post('/getReportedPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var jsonArray=[];
	var reported = "true";

	con.query('SELECT * FROM caregiverforumdata WHERE reported=? ORDER BY id DESC',[reported],
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
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})

//get posts by id
app.post('/getPostCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var id = post_data.id;
	var jsonArray=[];
	con.query('SELECT * FROM caregiverforumdata WHERE id=? ORDER BY id DESC',[id],
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
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})



//add to favourite list
app.post('/addToFavouriteCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var postID = post_data.postID;

	con.query('INSERT INTO favouriteListCaregiver (email,postID) VALUES (?,?)',
		[email,postID], 
		function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				res.json([{success:'1'}]);
			}
		})
})

//remove from favourite list
app.post('/removeFavouriteCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var postID = post_data.postID;

	con.query('DELETE FROM favouriteListCaregiver WHERE email=? AND postID=?',
		[email,postID], function(err, result, fields){
			if(err){
				res.json([{success: '0'}]);
			}
			else{
				res.json([{success: '1'}]);
			}
		});
})

//get is favourite 
app.post('/getIsFavouriteCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var postID = post_data.postID; 

	con.query('SELECT * FROM favouriteListCaregiver WHERE email=? AND postID=?',
		[email,postID], function(error,result,fields){
			if(error){
				res.json([{success:'0'}]);
			} else{
				if(result && result.length)	{
					res.json([{success: '1'}]);
				} else{
					res.json([{success:'-1'}]);
				}
			}
		})
})

//view favourite list
app.post('/myFavouriteListCaregiver/',(req,res,next)=>{
	var post_data = req.body;
	var email = post_data.email;
	var jsonArray=[];

	con.query('SELECT * FROM caregiverforumdata WHERE id in (SELECT postID FROM favouriteListcaregiver WHERE email=?) ORDER BY id DESC', [email],
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
					date: result[i].date,
					id: result[i].id});
			}
			res.json(jsonArray);
		} else{
			res.json([{success:'-1'}]);
		}
	});
})






//start server
const port = 3000
app.listen(port, ()=>{
	console.log(`Restful running on port ${port}`);
});


