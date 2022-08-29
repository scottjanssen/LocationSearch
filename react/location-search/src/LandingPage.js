import React from 'react';
import {Button, IconButton, InputAdornment, OutlinedInput, Stack, Typography} from "@mui/material";
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import './LandingPageStyles.css';

function LandingPage() {

    const [values, setValues] = React.useState({
        login: true,
        username: '',
        password: '',
        showPassword: false,
    });

    const handleChange = (prop) => (event) => {
        setValues({ ...values, [prop]: event.target.value });
    };

    const handleClickShowPassword = () => {
        setValues({
            ...values,
            showPassword: !values.showPassword,
        });
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleUserType = (event) => {
        setValues({
            ...values,
            login: !values.login
        })
    }

    return (
        <div className="landing-page-div">
            <Stack spacing={2}>
                <Typography variant="h4">Location Search</Typography>
                <Typography>This app returns all locations of interest
                    near a specified coordinate</Typography>
                <OutlinedInput
                    id="outlined-adornment-username"
                    type={'text'}
                    value={values.username}
                    onChange={handleChange('username')}
                    placeholder={"Username"}
                />
                <OutlinedInput
                    id="outlined-adornment-password"
                    type={values.showPassword ? 'text' : 'password'}
                    value={values.password}
                    onChange={handleChange('password')}
                    placeholder={"Password"}
                    endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={handleClickShowPassword}
                                onMouseDown={handleMouseDownPassword}
                                edge="end"
                            >
                                {values.showPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                        </InputAdornment>
                    }
                />
                <Stack direction={"row"} spacing={2}>
                    <Button variant={"contained"}>
                        {values.login ? "Login" : "Sign Up"}
                    </Button>
                    <Button variant={"outlined"} onClick={handleUserType}>
                        {values.login ? "Not a user?" : "Already a user?"}
                    </Button>
                </Stack>
            </Stack>
        </div>
    );
}

export default LandingPage;