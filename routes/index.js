import express from 'express';
import { 
    getUser, 
    Register,
    Login,
    redirectOauthLogin,
    callbackOauthLogin,
    forgotPassword,
    resetPassword,
    logout,
    getResetPassword,
    profile,

} from '../controller/UserController.js';

import { verifyToken } from '../middleware/VerifyToken.js';
import { refreshToken } from '../controller/RefreshToken.js';
import { fetchNews } from '../controller/NewsController.js';
import { hardLimiter } from '../middleware/limiter.js';
const router = express.Router();


router.get('/news', verifyToken, fetchNews);
router.get('/users',verifyToken, getUser);
router.get('/profile',verifyToken, profile)
router.post('/register', Register);
router.post('/login', Login);
router.post('/token', refreshToken);
router.get('/auth/google', redirectOauthLogin);
router.get('/auth/google/callback', callbackOauthLogin);

router.post('/forgot-password', hardLimiter, forgotPassword);
router.get('/reset-password/:id/:token', getResetPassword);
router.post('/reset-password/:id/:token', hardLimiter, resetPassword);

router.delete('/logout', logout);



export default router;