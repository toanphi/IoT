import { Layout, Select, DatePicker, Statistic, Card, Empty, Row, Col, message } from 'antd';
import { ArrowUpOutlined } from '@ant-design/icons';
import { Line, Bar, Radar } from '@ant-design/charts';
import React from 'react';

const { RangePicker } = DatePicker;

class DataAnalyzis extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            devices: [],
            selectedDevice: {},
            lstDeviceCommands: [],
            selectedData: [],
            deviceDataCount: [],
            deviceDataCountByHour: [],
            leastUsed : '',
            mostUsed: '',
            totalRecord: 0,
            startDate: '',
            endDate: ''
        }
    }

    componentDidMount() {
        try {
            this.fetchDevices();
        } catch (e){

        }
        
        // this.fetchDeviceData(this.state.selectedDevice.id);
    }

    fetchDevices = () => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow',
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device`, requestOptions)
            .then(response => {
                if (response.status === 200)
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                let newData = data.data
                for (let device in newData) {
                    newData[device].key = newData[device].name
                }

                let devices = newData.map(data => (
                    {
                        name: data.name,
                        productType: data.productType,
                        id: data.id
                    }
                ))
                this.setState({
                    devices: devices,
                    // selectedDevice: devices[0]
                })
            })
            .catch(error => console.log('error', error));
    }

    convertDateFormat = (d) => {
        var date_format_str = d.getFullYear().toString()+"-"+((d.getMonth()+1).toString().length===2?(d.getMonth()+1).toString():"0"+(d.getMonth()+1).toString())+"-"+(d.getDate().toString().length===2?d.getDate().toString():"0"+d.getDate().toString())+" "+(d.getHours().toString().length===2?d.getHours().toString():"0"+d.getHours().toString())+":"+((parseInt(d.getMinutes()/5)*5).toString().length===2?(parseInt(d.getMinutes()/5)*5).toString():"0"+(parseInt(d.getMinutes()/5)*5).toString())+":00";
        return date_format_str 
    }

    fetchCommandCountData = (deviceId, startDate, endDate) => {
        var inputData = {
            "deviceId": deviceId,
            "startDate": startDate,
            "endDate": endDate
        }

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        redirect: 'follow',
        body: JSON.stringify(inputData)
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/data/count`, requestOptions)
            .then(response => {
                if (response.status === 200)
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                var leastUsed = data.data.deviceDataCountByHourLst.reduce((prev, current) => (prev.count < current.count) ? prev : current).time
                var mostUsed = data.data.deviceDataCountByHourLst.reduce((prev, current) => (prev.count > current.count) ? prev : current).time
                this.setState({
                    leastUsed: leastUsed, 
                    mostUsed: mostUsed,
                    deviceDataCount: data.data.deviceDataCountLst,
                    deviceDataCountByHour: data.data.deviceDataCountByHourLst
                    // selectedDevice: devices[0]
                })

            })
            .catch(error => console.log('error', error));
    }

    fetchDeviceData = (deviceId, startDate, endDate) => {
        var startDateFormat = startDate
        var endDateFormat = endDate
        if (startDate !== '' && endDate !== '') {
            startDateFormat = this.convertDateFormat(startDate);
            startDateFormat = this.convertDateFormat(endDate);
        }
        
        var inputData = {
            "deviceId": deviceId,
            "startDate": startDateFormat,
            "endDate": endDateFormat
        }

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        redirect: 'follow',
        body: JSON.stringify(inputData)
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/data/data`, requestOptions)
            .then(response => {
                if (response.status === 200)
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                var selectedData = data.data
                this.setState({
                    selectedData: selectedData,
                })

                if (this.state.selectedDevice.productType !== "sensor") {
                    this.fetchCommandCountData(deviceId, startDateFormat, endDateFormat);
                }
            })
            .catch(error => console.log('error', error));
    }

    fetchDeviceCommands = (deviceType) => {
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
                var deviceCommand = data.data

                this.setState({
                    lstDeviceCommands: deviceCommand,
                })
            })
            .catch(error => console.log('error', error));
    }

    onSelectDeviceChange = (value) => {
        var selectedDevice = this.state.devices.filter(e => {
            return e.name === value
        })
        this.setState({
            selectedDevice: selectedDevice[0],
        })
        this.fetchDeviceCommands(selectedDevice[0].productType)
        this.fetchDeviceData(selectedDevice[0].id, this.state.startDate, this.state.endDate)
    }

    onSelectDeviceSearch = (val) => {

    }

    onDatePickerOk = (value) => {

        // var startDate = new Date(value[0]).toLocaleString( 'sv', { timeZoneName: 'short' });
        // var endDate = new Date(value[1]).toLocaleString( 'sv', { timeZoneName: 'short' });

        // this.setState({
        //     startDate: startDate.substring(0, startDate.lastIndexOf(" ")),
        //     endDate: endDate.substring(0, endDate.lastIndexOf(" "))
        // })
        // console.log(this.state.startDate, this.state.endDate)
    }

    onDatePickerChange = (value, dateString) => {
        if (dateString[0] === "" && dateString[1] === "") {
            // this.fetchDeviceData(this.state.selectedDevice.id, this.state.startDate, this.state.endDate);
            return
        }
        var startDate = new Date(dateString[0]);
        var endDate = new Date(dateString[1])
        this.fetchDeviceData(this.state.selectedDevice.id, startDate, endDate)
        this.setState({
            startDate: startDate,
            endDate: endDate
        })
    }

    render() {
        // var dateFilteredData = this.state.selectedData.filter(data => {
        //     var createdDate = new Date(data.createdDate);
        //     return +createdDate >= +this.state.startDate && +createdDate <= +this.state.endDate
        // })

        var lineData = this.state.selectedData.map(data => (
            {
                createdDate: data.createdDate,
                data: Number(data.data)
            }
        ))
        var lineChartConfig = {
            data: lineData,
            height: 600,
            padding: "auto",
            xField: 'createdDate',
            yField: 'data',
            xAxis: { tickCount: 2 },
            annotations: [
                {
                    type: 'regionFilter',
                    start: ['min', 'median'],
                    end: ['max', '0'],
                    color: '#F4664A',
                },
                {
                    type: 'text',
                    position: ['min', 'median'],
                    content: 'Average',
                    offsetY: -4,
                    style: { textBaseline: 'bottom' },
                },
                {
                    type: 'line',
                    start: ['min', 'median'],
                    end: ['max', 'median'],
                    style: {
                        stroke: '#F4664A',
                        lineDash: [2, 2],
                    },
                },
            ],
            label: {
                style: {
                    fill: '#aaa',
                },
            },
        };

        var columnChartData = this.state.selectedData.map(data => (
            {
                command: data.command,
                type: data.dataType
            }
        ))

        var columnChartConfig = {
            data: this.state.deviceDataCount,
            xField: 'count',
            yField: 'command',
            seriesField: 'command',
            legend: { position: 'top-left' },
        };

        var radarConfig = {
            data: this.state.deviceDataCountByHour,
            xField: 'time',
            yField: 'count',
            meta: {
                star: {
                    alias: 'command',
                    min: 0,
                    nice: true,
                },
            },
            xAxis: {
                line: null,
                tickLine: null,
            },
            yAxis: {
                label: false,
                grid: {
                    alternateColor: 'rgba(0, 0, 0, 0.04)',
                },
            },
            point: {},
            area: {},
        };

        if (this.props.isDataAnalyzis !== '2') {
            return ""
        }

        return <Layout style={{ background: "white" }}>
            <div style={{
                padding: '2px'
            }}>
                <div>
                    <Select
                        showSearch
                        style={{
                            width: "200px"
                        }}
                        optionFilterProp="children"
                        onChange={this.onSelectDeviceChange}
                        onSearch={this.onSelectDeviceSearch}
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        defaultValue={this.state.selectedDevice === undefined ? "" : this.state.selectedDevice.name}
                    >
                        {this.state.devices.map(value => (
                            <Select.Option value={value.name} key={value.name}>{value.name}</Select.Option>
                        ))}
                    </Select>

                    <RangePicker showTime placeholder={["Từ ngày", "Đến ngày"]} style={{ float: 'right', marginRight: '6px' }} onOk={this.onDatePickerOk} onChange={this.onDatePickerChange} />
                </div>
                {(this.state.selectedDevice.productType === undefined) ?
                    <Empty></Empty> : ""}
                {this.state.selectedDevice.productType === "sensor" ? <div style={{ margin: '10px 0px 20px 0px' }}>
                    <Line style={{
                        padding: '20px',
                        background: 'white',
                    }} {...lineChartConfig} /></div> : ""}
                {(this.state.selectedDevice.productType !== "sensor" && this.state.selectedDevice.productType !== undefined) ?
                    <div style={{
                        display: 'flex',
                        padding: '10px',
                        margin: '10px 0px 50px 0px',
                        // minHeight: '600px'
                    }}>
                        <div style={{ width: '100%' }}>
                            <label>Thống kê câu lệnh dùng thường xuyên</label>
                            <Bar
                                style={{
                                    padding: '20px',
                                    background: 'white',
                                    height: '92%'
                                }}
                                {...columnChartConfig}
                            />
                        </div>
                        <div style={{ width: '100%' }}>
                            <label>Khoảng thời gian sử dụng</label>
                            <Radar style={{ marginBottom: '40px' }} {...radarConfig} />
                            <Row style={{
                                marginTop: '20px',
                                // width: '30%',
                                // float: 'right'
                            }}>
                                <Col span={12}>
                                    <Statistic
                                        title="Sử dụng nhiều nhất"
                                        value={this.state.mostUsed}
                                        precision={2}
                                        valueStyle={{ color: '#3f8600' }}
                                    // prefix={<ArrowUpOutlined />}
                                    // suffix="%"
                                    />
                                </Col>
                                <Col span={12}>
                                    <Statistic
                                        title="Sử dụng ít nhất"
                                        value={this.state.leastUsed}
                                        precision={2}
                                        valueStyle={{ color: '#3f8600' }}
                                    // prefix={<ArrowUpOutlined />}
                                    // suffix="%"
                                    />
                                </Col>
                            </Row>
                        </div>

                    </div> : ""}
            </div>

            {/* <div>
                <DAStatistic title={this.state.selectedData[0].dataType} value={-10} noc={this.state.selectedData.length} totalRecord={this.state.totalRecord}/>
            </div> */}
        </Layout>
    }
}

export default DataAnalyzis;