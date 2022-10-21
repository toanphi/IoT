import React from 'react';
import {message} from 'antd'
import '../css/manageLirc.css';

class ManageLircConfig extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            isInit: false,
            lstCommands: [],
            deviceType: "",
            isBtnDisable: false
        }
    }

    componentDidMount() {
        
    }

    mapProductType = {
        "tivi": "tivi",
        "quạt": "fan",
        "điều hòa": "air-conditioner"
    }
    lstProductTypes = ['tivi', 'quạt', 'điều hòa']
    
    onDeviceTypeSelect = (event) => {
        var productType = this.mapProductType[event.target.value]
        this.callDeviceCommand(productType);
        this.callInitRecordingSession()
    }

    onCommandClick = (event) => {
        this.setState({
            isBtnDisable: true
        })
        this.callStartRecordingProcess(event.target.value)
    }

    onFinishRecord = () => {
        this.callFinishRecording()
    }

    callInitRecordingSession = () => {
        var configName = document.getElementById("config-name").value;

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow',
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/lirc/${configName}`, requestOptions)
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        isInit: true
                    });
                    message.success("Bắt đầu thực hiện lưu thông tin điều khiển")
                } else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } else {
                    message.error("Có lỗi xảy ra")
                }     
            });
    }

    callStartRecordingProcess = (btnName) => { 
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        redirect: 'follow',
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/lirc/${btnName}`, requestOptions)
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        isBtnDisable: false
                    })
                    message.success(`Lưu thành công phím chức năng ${btnName}`)

                } else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } else {
                    this.setState({
                        isBtnDisable: false
                    })
                    message.error("Vui lòng thử lại")
                }
                    
            })
    }

    callFinishRecording = () => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'DELETE',
        headers: myHeaders,
        redirect: 'follow',
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/lirc`, requestOptions)
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        isInit: false,                   
                    })
                    message.success("Đã hoàn thành lưu thông tin điều khiển thiết bị")
                } else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } else {
                    message.error("Đã xảy ra lỗi, vui lòng thử lại")
                }
                    
                this.setState({
                    isBtnDisable: false,
                })
            })
    }

    callDeviceCommand = (deviceType) => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow',
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/command/${deviceType}`, requestOptions)
            .then(response => {
                if (response.status === 200)
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                this.setState({
                    lstCommands: data.data
                })
            });
        
    }

    onBackBtnClick = () => {
        this.setState({
            isInit: false
        })
    }

    initPage = (item) => {
        return <div style={{display:"grid", height: '70vh'}}>
            {/* <div style={{height: "fit-content"}}>
                <h4 style={{height:"fit-content"}}>Thêm thông tin điều khiển vào hệ thống theo các bước:
                    <h5>&emsp;Bước 1: Điền tên thiết bị (Không dấu, nên dùng tên hãng thiết bị và mã thiết bị)</h5>
                    <h5>&emsp;Bước 2: Sau khi đã nhập tên hãy bấm chọn một trong số những loại thiết bị ở bên dưới</h5>
                    <h5>&emsp;Bước 3: Tiếp theo một bảng mẫu sẽ lần lượt hiện ra các nút được hỗ trợ, bấm nút để bắt đầu quá trình lưu thông tin</h5>
                    <h5>&emsp;Bước 4: Hướng điều khiển thiết bị đến mắt đọc của hệ thống và nhấn 2 lần nút được chỉ định</h5>
                    <h5>&emsp;Bước 5: Chọn lưu lại và trở lại trang thống kê thiết bị để tiến hành đăng ký</h5>
                </h4>
            </div> */}
            <h3 style={{textAlign: 'center', height: 'fit-content'}}>Thêm thông tin điều khiển thiết bị hồng ngoại</h3>
            <div style={{
                marginTop: "-6%",
                width: '100%',
                height: 'fit-content', 
                display: 'flex', 
                justifyContent: 'center'}}>
                <input id="config-name" placeholder="Tên thiết bị sẽ được lưu" style={{
                    width: "20%", 
                    height:"60px", 
                    borderRadius: "20px", 
                    padding: "5px",
                    margin: "auto"}}/>
            </div>
            <div style={{display: "flex", width: "100%", height: "100%", marginTop: "-6%"}}>
                {item.map(item => (
                    <div style={{width: "100%", display: 'flex', justifyContent: 'center'}}>
                        <button key={item} style={{width: '80%', height:'80%', margin: 'auto', borderRadius: '20px'}} onClick={this.onDeviceTypeSelect} value={item}>{item}</button>
                    </div>   
                ))}
            </div>
        </div>
    }

    recordPage = () => {
        return <div>
            <div style={{display:"flex"}}>
                <div>
                    <button href="#" onClick={this.onBackBtnClick} class="back-button">Quay lại</button>
                </div>
            </div>
           
            <div class= "button-wrap">
                {this.state.lstCommands.map(cmd => (
                    <button key={cmd.id} class="button-list" value={cmd.command} onClick={this.onCommandClick} disabled={this.state.isBtnDisable}>{cmd.command}</button>
                ))}
            </div>
            <div class="save-button-wrap">
                <button onClick={this.onFinishRecord} class="save-button">Lưu</button>
            </div> 
        </div>
    }

    render() {

        var isInit = this.state.isInit
        console.log(this.state.lstCommands)
        return <div>
            {isInit === false ? this.initPage(this.lstProductTypes) : this.recordPage()}
        </div>
    }
}

export default ManageLircConfig;
