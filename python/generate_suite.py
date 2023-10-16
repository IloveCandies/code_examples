import  os
from jinja2 import Template

SUITE_TEMPLATE= Template("""<?xml version = "1.0" encoding = "UTF-8"?>
    <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
    <suite name="ELTA" thread-count = "1">                
        <listeners>
            <listener class-name="listeners.AnnotationTransformer"/>
        </listeners>
        <parameter name="TESTRAIL" value="{{testrail}}" />
        <parameter name="TESTRAIL_URL" value="{{testrail_url}}" />
        <parameter name="TESTRAIL_LOGIN" value="{{testrail_login}}" />
        <parameter name="TESTRAIL_PASSWORD" value="{{testrail_password}}" />
        <parameter name="TESTRAIL_TESTRUN" value="{{testrail_testrun}}" />
        <parameter name="BUILD" value="{{build}}" />
        <parameter name="DEVICE_MODEL" value="{{device_model}}" />
        <parameter name="PLATFORM_NAME" value="{{platform_name}}" />
        <parameter name="PLATFORM_VERSION" value="{{platform_version}}" />
        <parameter name="DEVICE_NAME" value="{{device_name}}" />
        <parameter name="APP_PACKAGE" value="{{app_package}}" />
        <parameter name="APP" value="{{app}}" />
        <parameter name="APPIUM_URL" value="{{appium_url}}" />
        <parameter name="MITMDUMP_PATH" value="{{mitmdump_path}}" />
        <test name = "{{suite}}">
            <classes>
            {%  for key, value in test_classes.items() %}
            <class name = "{{key}}" > 
                    <methods>{% for method in value %} 
                            <include name="{{method}}"/>{% endfor %}
                         </methods>
                    </class> {% endfor %}
            </classes>
        </test>
    </suite> 
    """)


def get_devices():
    dev_list = os.popen("adb devices -l").read()
    dev_list = dev_list.replace("List of devices attached\n", "")
    dev_list = dev_list.split("\n")
    devices = {}
    for i in range (len(dev_list)):
        serial_number = dev_list[i].split(" ")[0]
        if serial_number != "":
            devices[i] = serial_number
    return devices

def generate_caps(serial_number):
    dev_info  = {}
    dev_info["PLATFORM_NAME"] = "Android"
    dev_info["PLATFORM_VERSION"] = os.popen("adb -s " + serial_number +" shell getprop ro.build.version.release").read()
    dev_info["PLATFORM_VERSION"]= dev_info["PLATFORM_VERSION"].replace("\n","")
    dev_info["DEVICE_NAME"] = os.popen("adb -s " + serial_number +" shell getprop ro.product.product.model").read()
    dev_info["DEVICE_NAME"]= dev_info["DEVICE_NAME"].replace("\n","")
    dev_info["DEVICE_MODEL"] = os.popen("adb -s " + serial_number +" shell getprop ro.product.product.model").read()
    dev_info["DEVICE_MODEL"]= dev_info["DEVICE_NAME"].replace("\n","")
    dev_info["TESTRAIL"] = str(input("Флаг подключения к testrail введите true если хотите подключить - по умолчнию false: ") or "false")
    
    if dev_info["TESTRAIL"] == "true":
        dev_info["TESTRAIL_URL"] = input("Testrail URL: ")
        dev_info["TESTRAIL_LOGIN"] = input("Testrail Login: ")
        dev_info["TESTRAIL_PASSWORD"] = input("Testrail Password: ")
        dev_info["TESTRAIL_TESTRUN"] = input("Testrail Testrun: ")
    else:
        dev_info["TESTRAIL_URL"] = ""
        dev_info["TESTRAIL_LOGIN"] = ""
        dev_info["TESTRAIL_PASSWORD"] = ""
        dev_info["TESTRAIL_TESTRUN"] = ""

    dev_info["APPIUM_URL"] = str(input("Путь к устройству для Appium - по умолчнию http://localhost:4723/wd/hub: ") or "http://localhost:4723/wd/hub")
    dev_info["APP"] = str(input("Путь к APK - по умолчнию D:\\git\\ELTA-automation\\app\\2.1.0.4-dev(275).apk: ") or "D:\\git\\ELTA-automation\\app\\2.1.0.4-dev(275).apk")
    dev_info["APP_PACKAGE"] = str(input("Package name - по умолчннию: com.elta.android: ") or "com.elta.android")
    dev_info["BUILD"] = str(input("Build - по умолчннию: 2.1.0.4-dev(275): ") or "2.1.0.4-dev(275)")
    dev_info["MITMDUMP_PATH"] = str(input("Путь к mitm dump - по умолчннию: /opt/homebrew/Cellar/mitmproxy/9.0.1/bin/mitmdump: ") or "/opt/homebrew/Cellar/mitmproxy/9.0.1/bin/mitmdump")
    return dev_info

def get_class_methods(file_path):
    methods = []
    with open(file_path, "r", encoding="utf-8") as f:
        tree = f. read().split("\n")
    for string in tree:
        if "//" not in string:
            if "public void test" in string:
                temp = string.replace("    public void ","")
                temp = temp.replace("() {","")
                methods.append(temp)
    return methods

def get_test_classes():
    classes = {}
    suite_name = input("Введите название suite: ")
    while True:
        test_class = input("Введите название тестового класса или break для выхода: ")
        if test_class == "break":
            break
        else:
            input_methods_type = int(input("Введите 1 - для ввода названией классов вручную; 2 - Для получения всех методов из файла: "))
            if(input_methods_type == 1):
                class_methods == input("Введите названия методов через запятую: ")
                classes[test_class] = class_methods.split(",")
            if(input_methods_type == 2):
                classes[test_class] = get_class_methods(input("Введите путь к файлу в формате С:\\folder\\JavaClass.java "))
    return suite_name, classes
        


def generate_suite(device_caps, suite_template, suite_xml_path):
    suite_name,classes = get_test_classes()
    xml = suite_template.render(
        testrail= device_caps["TESTRAIL"],
        testrail_url = device_caps["TESTRAIL_URL"],
        testrail_login = device_caps["TESTRAIL_LOGIN"],
        testrail_password = device_caps["TESTRAIL_PASSWORD"],
        testrail_testrun= device_caps["TESTRAIL_TESTRUN"],
        build = device_caps["BUILD"],
        device_model = device_caps["DEVICE_MODEL"],
        platform_name = device_caps["PLATFORM_NAME"],
        platform_version= device_caps["PLATFORM_VERSION"],
        device_name = device_caps["DEVICE_NAME"],
        app_package = device_caps["APP_PACKAGE"],
        app  = device_caps["APP"],
        appium_url = device_caps["APPIUM_URL"],
        mitmdump_path = device_caps["MITMDUMP_PATH"],
        suite = suite_name,
        test_classes = classes )

    print(xml)

    with open(suite_xml_path, "w") as fw:
        fw.write(xml)

devices = get_devices()
print("Подключенные устройства: ")
print(devices)
device_key = int(input("Введите id девайса для получения информаци: "))
device_info = generate_caps(devices[device_key])
print(device_info)
suite_xml_path = str(input("Введите путь для сохранения сгенерированного xml, по умолчанию C:\device_suite_generator\suite-android.xml") or "C:\\device_suite_generator\\suite-android.xml")
generate_suite(device_info,SUITE_TEMPLATE, suite_xml_path)
