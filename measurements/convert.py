import csv

f = open('log', 'r')
lines = f.readlines()

api_service = []
customers_service = []
vets_service = []
visits_service = []

counter = 0
for line in lines:
    if 'NAME' in line:
        counter = 0
        continue

    data = line.split()
    record = {
        'cpu': int(data[1].replace('m', '')),
        'mem': int(data[2].replace('Mi', ''))
    }

    if counter == 0:
        api_service.append(record)
    elif counter == 1:
        customers_service.append(record)
    elif counter == 2:
        vets_service.append(record)
    elif counter == 3:
        visits_service.append(record)
    counter += 1


time = [''] + [str(x*1).replace('.', ',') for x in range(2*0, len(api_service))]
customers_cpu = [x['cpu'] for x in customers_service]
vets_cpu = [x['cpu'] for x in vets_service]
visits_cpu = [x['cpu'] for x in visits_service]

write = csv.writer(open('metric.csv', 'w'), delimiter=';')
rows = [
    time,
    #['api_service_cpu_m'] + [x['cpu'] for x in api_service],
    ['customers_service_cpu_m'] + [0]*(len(time) - len(customers_cpu)) + customers_cpu,
    ['vets_service_cpu_m'] + [0]*(len(time) - len(vets_cpu)) + vets_cpu,
    ['visits_service_cpu_m'] + [0]*(len(time) - len(visits_cpu)) + visits_cpu,

    [''],

    time,
    #['api_service_mem_Mi'] + [x['mem'] for x in api_service],
    ['customers_service_mem_Mi'] + [0]*(len(time) - len(customers_cpu)) + [x['mem'] for x in customers_service],
    ['vets_service_mem_Mi'] + [0]*(len(time) - len(vets_cpu)) + [x['mem'] for x in vets_service],
    ['visits_service_mem_Mi'] + [0]*(len(time) - len(visits_cpu)) +  [x['mem'] for x in visits_service],
]
write.writerows(rows)
