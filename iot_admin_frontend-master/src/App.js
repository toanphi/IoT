import React from 'react';

import DevicesOverview from './pages/DevicesOverview';
import DataAnalyzis from './pages/DataAnalyzis';
import ManageLircConfig from './pages/ManageLircConfig';
import LoginPage from './pages/LoginPage';
import ManageCommand from './pages/ManageCommand';

import { Layout, Menu, Breadcrumb } from 'antd';

const { Header, Content, Footer } = Layout;

class Admin extends React.Component {
    state = {
        isLogin: localStorage.getItem("accessToken") != null,
        menuFlag: '1',
        breadCrumb: ['Admin', 'Thiết bị'],
    };

    render() {
        console.log(this.state.isLogin)
        return !this.state.isLogin ? <LoginPage></LoginPage> : <Layout style={{ minHeight: '100vh', minWidth: '1600px'}}>
        <Header style={{ position: 'relative', zIndex: 1, width: '100%' }} collapsedWidth = "100" collapsed="true">
            <div className="logo"  style={{ float: 'left', width: 'auto', color: 'white' }}>IOT Admin</div>
            <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']} style={{ float: "right", width: "fit-content"}}>
                <Menu.Item onClick={this.menuItemOnclick} key="1" href="/">Thống kê thiết bị</Menu.Item>
                <Menu.Item onClick={this.menuItemOnclick} key="2">Thông số dữ liệu</Menu.Item>
                <Menu.Item onClick={this.menuItemOnclick} key="3">Hồng ngoại</Menu.Item>
                {localStorage.getItem("accessRole") === "admin" ? <Menu.Item onClick={this.menuItemOnclick} key="4">Điều khiển</Menu.Item> : null}
                <Menu.Item onClick={this.menuItemOnclick} key="5">Đăng xuất</Menu.Item>             
            </Menu>
        </Header>
        <Content className="site-layout" style={{ padding: '0 50px', border: 'none', borderRadius: '5px', margin: '50px 30px 0px 30px'}}>
            <div style={{width: '100%', borderRadius: '5px 5px 0px 0px' , background: '#777' }}>
                <Breadcrumb style={{ padding: '16px 0px 16px 16px' }}>
                    {this.state.breadCrumb.map(item => (
                        <Breadcrumb.Item key={item}>{item}</Breadcrumb.Item>
                    ))}
                </Breadcrumb>
                {/* <div style={{ padding: '16px 0px 16px 16px' }}>Content</div> */}
            </div>

            <div className="site-layout-background" style={{ padding: 24, minHeight: 380, background:"white", borderRadius: '0px 0px 5px 5px' }}>
                {this.state.menuFlag === '1' ?  <DevicesOverview isDevicesOverview={this.state.menuFlag}/> : ""}
                {this.state.menuFlag === '2' ? <DataAnalyzis isDataAnalyzis={this.state.menuFlag}/> : ""}
                {this.state.menuFlag === '3' ? <ManageLircConfig isManageLircConfig={this.state.menuFlag} /> : ""}   
                {this.state.menuFlag === '4' ? <ManageCommand isManageCommand={this.state.menuFlag}/> : ""} 
            </div>

        </Content>
        <Footer style={{ textAlign: 'center' }}>IOT admin @Designed by NvMe</Footer>
    </Layout>
    }

    menuItemOnclick = (event) => {
        
        this.setState({
            menuFlag: event.key
        })
        if (event.key === "1"){
            this.setState({
                breadCrumb: ['Admin', 'Thiết bị']
            })
        }

        if (event.key === "2"){
            this.setState({
                breadCrumb: ['Admin', 'Dữ liệu']
            })
        }
        if (event.key === "3"){
            this.setState({
                breadCrumb: ['Admin', 'Hồng ngoại']
            })
        }
        if (event.key === "4"){
            this.setState({
                breadCrumb: ['Admin', 'Điều khiển']
            })
        }
        if (event.key === "5"){
            localStorage.removeItem("accessToken")
            localStorage.removeItem("accessRole")
            window.location.reload();
        }
        
    }
}

export default Admin;