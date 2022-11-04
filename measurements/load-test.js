import http from 'k6/http';
import { check, group, sleep as _sleep } from 'k6';

export const options = {
    vus: 10,
    iterations: 500,
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(95)', 'count'],
};

const sleep = () => _sleep(0.1);
const BASE_URL = 'http://127.0.0.1:43711';
const PAYLOAD_PARAMS = {
    headers: {
        'Content-Type': 'application/json',
    },
};

let user_id;
let pet_id;

function test_vets_service() {
    const VETS_API = '/api/vet/vets';

    // GET all vets
    const get_vets_response = http.get(BASE_URL + VETS_API);
    check(get_vets_response, {
        'vets-service: GET all vets': (r) => r.status === 200
    });
    sleep();
}

function test_customers_service() {
    const CUSTOMERS_API = '/api/customer/owners';

    // GET all customers
    const get_owners_response = http.get(BASE_URL + CUSTOMERS_API);
    check(get_owners_response, {
        'customer-service: GET all customers': (r) => r.status === 200
    });
    sleep();

    // POST new customer
    let body = JSON.stringify({
        firstName: 'test_firstName' + __ITER,
        lastName: 'test_lastName' + __ITER,
        address: 'test_address' + __ITER,
        city: 'test_city' + __ITER,
        telephone: '0000000000000'
    });
    const post_customer_response = http.post(BASE_URL + CUSTOMERS_API, body, PAYLOAD_PARAMS);
    check(post_customer_response, {
        'customer-service: POST one customer': (r) => r.status === 201
    });
    user_id = post_customer_response.json().id;
    sleep();

    // GET registered customer
    const get_owner_response = http.get(BASE_URL + `/#!/owners/details/${user_id}`);
    check(get_owner_response, {
        'customer-service: GET one customer': (r) => r.status === 200
    });
    sleep();

    // PUT telephone number
    body = JSON.parse(body);
    body['telephone'] = __ITER;
    body = JSON.stringify(body);
    const put_owner_response = http.put(BASE_URL + CUSTOMERS_API + '/' + user_id, body, PAYLOAD_PARAMS);
    check(put_owner_response, {
        'customer-service: PUT one customer': (r) => r.status === 204
    });
    sleep();

    // POST pet
    body = JSON.stringify({
        'name': 'Cat' + __ITER,
        'birthDate':  (new Date()).toISOString(),
        'typeId': '1'
    })
    const post_pet_response = http.post(BASE_URL + `/api/customer/owners/${user_id}/pets`, body, PAYLOAD_PARAMS);
    check(post_pet_response, {
        'customer-service: POST one pet': (r) => r.status === 201
    });
    pet_id = post_pet_response.json().id;
    sleep();

    // GET pet
    const get_pet_response = http.get(BASE_URL + `/api/customer/owners/${user_id}/pets/${pet_id}`, body, PAYLOAD_PARAMS);
    check(get_pet_response, {
        'customer-service: GET one pet': (r) => r.status === 200
    });
    sleep();

    // PUT pet
    body = JSON.stringify({
        'id': pet_id,
        'name': 'Cat' + __ITER,
        'birthDate':  (new Date()).toISOString(),
        'typeId': '1'
    })
    const put_pet_response = http.put(BASE_URL + `/api/customer/owners/${user_id}/pets/${pet_id}`, body, PAYLOAD_PARAMS);
    check(put_pet_response, {
        'customer-service: PUT one pet': (r) => r.status === 204
    });
    sleep();
}

function test_visits_service() {
    const body = JSON.stringify({
        'date': (new Date()).toISOString(),
        'description': 'description' + __ITER
    });
    const post_visit_response = http.post(BASE_URL + `/api/visit/owners/${user_id}/pets/${pet_id}/visits`, body, PAYLOAD_PARAMS)
    check(post_visit_response, {
        'visits-service: POST one visit': (r) => r.status === 201 || r.status === 200
    });
    sleep();

    const get_visits_response = http.get(BASE_URL + `/api/visit/owners/${user_id}/pets/${pet_id}/visits`)
    check(get_visits_response, {
        'visits-service: GET all visit': (r) => r.status === 200
    });
    sleep();
}

export default function () {
    group('user journey', (_) => {
        test_vets_service();
        test_customers_service();
        test_visits_service();
    });
}
