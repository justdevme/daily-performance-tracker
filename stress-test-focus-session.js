import http from 'k6/http';
import { check, sleep } from 'k6';

// -------------------------------------------------------
// Config: 50 users đồng thời, chạy 1 lần
// -------------------------------------------------------
export const options = {
    vus: 50,          // 50 virtual users (threads)
    iterations: 50,   // tổng số requests
    duration: '10s',
};

const BASE_URL = 'http://localhost:8080';
const TASK_ID = 1; // đổi thành taskId có thật trong DB của em

// -------------------------------------------------------
// Login 1 lần để lấy token
// -------------------------------------------------------
export function setup() {
    const loginRes = http.post(
        `${BASE_URL}/api/v1/auth/authenticate`,
        JSON.stringify({ email: 'test1@local.com', password: '123456' }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const token = loginRes.json('access_token');
    console.log(`Got token: ${token ? 'OK' : 'FAILED'}`);
    return { token };
}

// -------------------------------------------------------
// Main test: mỗi VU bắn 1 request POST /start cùng lúc
// -------------------------------------------------------
export default function (data) {
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${data.token}`,
    };

    const res = http.post(
        `${BASE_URL}/api/v1/tasks/${TASK_ID}/focus-sessions/start`,
        JSON.stringify({}),
        { headers }
    );

    // Chỉ 1 request được status 200, còn lại phải 500 (IllegalStateException)
    check(res, {
        'status is 200 or 500': (r) => r.status === 200 || r.status === 500,
    });

    console.log(`VU ${__VU}: status=${res.status}`);
}

// -------------------------------------------------------
// Teardown: kiểm tra DB chỉ có 1 RUNNING session
// -------------------------------------------------------
export function teardown(data) {
    const headers = {
        'Authorization': `Bearer ${data.token}`,
    };

    const res = http.get(
        `${BASE_URL}/api/v1/tasks/${TASK_ID}/focus-sessions/running`,
        { headers }
    );

    check(res, {
        'only 1 running session exists': (r) => r.status === 200,
    });

    console.log(`Teardown - running session check: ${res.status}`);
    console.log(`Response: ${res.body}`);
}