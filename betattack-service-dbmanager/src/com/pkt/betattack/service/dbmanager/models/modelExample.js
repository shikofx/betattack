
const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const bcrypt = require("bcryptjs");

    const userSchema = new Schema({
      firstName: {
        trim: true,
        type: String,
        required: [true, "firstName is required!"],
        validate(value) {
          if (value.length < 2) {
            throw new Error("firstName is invalid!");
          }
        }
      },
      lastName: {
        trim: true,
        type: String,
        required: [true, "lastName is required!"],
        validate(value) {
          if (value.length < 2) {
            throw new Error("lastName is invalid!");
          }
        }
      },
      username: {
        unique: [true, "Username already available"],
        type: String,
        required: [true, "Username is required"],
        validate(value) {
          if (value.length < 10) {
            throw new Error("Username is invalid!");
          }
        }
      },
      mobile: {
        unique: [true, "Mobile Number alraedy available"],
        type: String,
        required: [true, "Mobile Number is required"],
        validate(value) {
          if (value.length !== 10) {
            throw new Error("Mobile Number is invalid!");
          }
        }
      },
      password: {
        type: String,
        required: [true, "Password is required"],
        validate(value) {
          if (value.length < 8) {
            throw new Error("Password is invalid!");
          }
        }
      },
      gender: {
        type: String
      },
      dob: {
        type: Date,
        default: Date.now()
      },
      address: {
        street: {
          type: String
        },
        city: {
          trim: true,
          type: String
        },
        pin: {
          trim: true,
          type: Number,
          validate(value) {
            if (!(value >= 100000 && value <= 999999)) {
              throw new Error("Pin is invalid!");
            }
          }
        }
      },
      date: { type: Date, default: Date.now }
    });