// Import package
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');


//Password ULTILS
//CREATE FUNCTION TO RANDOM SALT
var genRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
        .toString('hex') /* convert to hexa format */
        .slice(0,length);
}

var sha512 = function (password,salt){
    var hash = crypto.createHmac('sha512',salt);
    hash.update(password);
    var value = hash.digest('hex');
    return{
        salt:salt,
        passwordHash:value

    };

};

function saltHashPassword(userPassword){
    var salt = genRandomString(16); //create 16 random characters
    var passwordData = sha512(userPassword,salt);
    return passwordData;

}

function checkHashPassword(userPassword,salt){
    var passwordData =sha512(userPassword,salt);
    return passwordData;
}

//express Service creation

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//Mongodb Client Creation

var MongoClient = mongodb.MongoClient;

//connection URL
var url = 'mongodb://localhost:27017'  //default port
MongoClient.connect(url,{useNewUrlParser: true}, function(err,client){
    if(err)
        console.log('Unable to connect to the MongoDB server.Error', err);
    else{
        //Register
        app.post('/register',(request,response,next,)=>{
            var post_data = request.body;

            var plaint_password = post_data.password;
            var hash_data = saltHashPassword(plaint_password);

            var password = hash_data.passwordHash;//Save password hash
            var salt = hash_data.salt; //save salt

            var name = post_data.name;
            var email = post_data.email;

            var insertJson ={
                'email': email,
                'password': password,
                'salt': salt,
                'name': name
            };
            var db = client.db('edmtdevnotejs');
            db.collection('user')
                .find({'email':email}).count(function (err, number){
                    if(number != 0){
                        response.json('Email already exists');
                        console.log('Email already exists');
                    }
                    else{
                        //Insert Data
                        db.collection('user')
                            .insertOne(insertJson,function (error,res){
                                response.json('Registration Succesful');
                                console.log('Registration Succesful');
                            })
                    }
            })
        });
        app.post('/login',(request,response,next,)=>{
            var post_data = request.body;


            var email = post_data.email;
            var userPassword = post_data.password;



            var db = client.db('edmtdevnotejs');
            //https://www.youtube.com/watch?v=4Xq2FUJvE-c
            //17:59
            db.collection('user')
                .find({'email':email}).count(function (err, number){
                if(number == 0){
                    response.json('Email doesnt exists');
                    console.log('Email doesnt exists');
                }
                else{
                    //Insert Data
                    db.collection('user')
                        .findOne({'email':email},function (err,user){
                            var salt = user.salt;
                            var hashed_password = checkHashPassword(userPassword,salt).passwordHash;
                            var encrypted_password = user.password;
                            if(hashed_password == encrypted_password){
                                response.json('Login Success');
                                console.log('Login Success');
                            }
                            else{
                                response.json('Wrong Login');
                                console.log('Wrong Login');
                            }
                        })
                }
            })
        });
        //start web server
        app.listen(3000,() => {
            console.log('Connected to MongoDB Server, Webservice Running on port 3000');
        })
    }
});

