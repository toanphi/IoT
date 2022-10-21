import { Form, Input, Button, Checkbox, message } from 'antd';
import React from 'react';

export default class LoginPage extends React.Component{

    constructor(props) {
        super(props)

        this.state = {
            username: "",
            password: ""
        }
    }

    fetchAuthenticate = (username, password) => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "password": password,
            "username": username
        });

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/authenticate`, requestOptions)
            .then(response => {
                if (response.status === 200) {
                    return response.json()
                }
            })
            .then(response => {
                console.log(response)
                localStorage.setItem("accessToken", response.data.jwt)
                localStorage.setItem("accessRole", username)
                window.location.reload();
            })
            .catch(error => message.error("Tài khoản hoặc mật khẩu không chính xác"));
    }

    onFinish = (values) => {
        this.fetchAuthenticate(values.username, values.password)
        console.log('Success:', values);
    };

    onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };
    
    render() {
        return <div>
            <div style={{width: "100%", display: "flex", justifyContent: "center", paddingTop: "2%"}}>
                <h1 style={{}}>Đăng nhập</h1>
            </div>
            <div style={{width: "100%", display: "flex", justifyContent: "center", paddingTop: "2%"}}>
                <Form
                    name="basic"
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 10 }}
                    initialValues={{ remember: true }}
                    onFinish={this.onFinish}
                    onFinishFailed={this.onFinishFailed}
                    autoComplete="off"
                    style={{width: "30%", height: "70%", border: "1px solid black", paddingTop: "40px"}}
                >
                    <Form.Item
                    label="Tài khoản"
                    name="username"
                    rules={[{ required: true, message: 'Điền thông tin tài khoản' }]}
                    >
                    <Input />
                    </Form.Item>
            
                    <Form.Item
                    label="Mật khẩu"
                    name="password"
                    rules={[{ required: true, message: 'Điền thông tin mật khẩu' }]}
                    >
                    <Input.Password />
                    </Form.Item>
            
                    <Form.Item name="remember" valuePropName="checked" wrapperCol={{ offset: 8, span: 16 }}>
                    <Checkbox>Nhớ tài khoản?</Checkbox>
                    </Form.Item>
            
                    <Form.Item wrapperCol={{ offset: 10, span: 12 }}>
                    <Button type="primary" htmlType="submit">
                        Đăng nhập
                    </Button>
                    </Form.Item>
                </Form>
            </div>    
        </div>
    }
}