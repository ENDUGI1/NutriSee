import express from 'express';
import { 
    getUser, 
    Register,
    Login,
    testNanoid,
    redirectOauthLogin,
    callbackOauthLogin,
    logout,

} from '../controller/User.js';

import { verifyToken } from '../middleware/VerifyToken.js';
import { refreshToken } from '../controller/RefreshToken.js';
import { fetchNews } from '../controller/NewsController.js';

const router = express.Router();


router.get('/news', verifyToken, fetchNews);
router.get('/users',verifyToken, getUser);
router.post('/register', Register);
router.post('/login', Login);
router.get('/token', refreshToken);
router.get('/auth/google', redirectOauthLogin);
router.get('/auth/google/callback', callbackOauthLogin);
router.delete('/logout', logout);



// router.get('/test-delay', (req,res) => {
//     const timeout = [50,100, 200,300];
//     const rand = Math.floor(Math.random() * timeout.length);
//     const randomTimeout = timeout[rand];
//     setTimeout(() => {
//         res.send("testing timeout");
//     }, randomTimeout)
// })

router.get('/testNanoid', testNanoid);

export default router;